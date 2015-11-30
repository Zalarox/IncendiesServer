package incendius.processors;

import java.awt.EventQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import incendius.game.items.GroundItem;
import incendius.handlers.ItemHandler;
import incendius.world.map.Region;

public class Item {

	private ScheduledExecutorService thread = Executors.newSingleThreadScheduledExecutor();

	private Region region;

	public Item(Region region) {
		this.region = region;
	}

	public void run() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				thread.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						CopyOnWriteArrayList<GroundItem> toRemove = new CopyOnWriteArrayList<GroundItem>();
						for (int j = 0; j < region.floorItems.size(); j++) {
							if (region.floorItems.get(j) != null) {
								GroundItem item = region.floorItems.get(j);
								if (item.hideTicks > 0) {
									try {
										item.hideTicks--;
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								if (item.hideTicks == 1 && !item.stopRemoval) {
									item.hideTicks = 0;
									ItemHandler.createGlobalItem(item, region);
									item.removeTicks = ItemHandler.HIDE_TICKS;
								}
								if (item.stopRemoval && item.hideTicks > 0) {
									item.hideTicks = 0;
									item.removeTicks = 0;
									ItemHandler.createGlobalItem(item, region);
								}
								if (item.removeTicks > 0) {
									item.removeTicks--;
								}
								if (item.removeTicks == 1 && (!item.stopRemoval || item.itemId == 592)) {
									item.removeTicks = 0;
									toRemove.add(item);
									ItemHandler.removeGlobalItem(item, item.getItemId(), item.getItemX(),
											item.getItemY(), item.heightLevel, item.getItemAmount(), region);
								}
								if (item.remove) {
									item.hideTicks = 0;
									item.removeTicks = 0;
									toRemove.add(item);
									ItemHandler.removeGlobalItem(item, item.getItemId(), item.getItemX(),
											item.getItemY(), item.heightLevel, item.getItemAmount(), region);
								}
							}
						}
						for (GroundItem i : toRemove) {
							ItemHandler.removeGlobalItem(i, region);
						}
						if (region.floorItems.size() <= 0) {
							region.itemProcessing = false;
							thread.shutdownNow();
						}
					}

				}, 0, 600, TimeUnit.MILLISECONDS);
			}
		});
	}

}
