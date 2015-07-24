@echo off
title Server
"C:\Program Files\Java\jdk\bin\java.exe" -Xmx1024m -cp bin;deps/poi.jar;deps/mysql.jar;deps/gtlvote.jar;deps/mina.jar;deps/slf4j.jar;deps/slf4j-nop.jar;deps/jython.jar;log4j-1.2.15.jar; main.GameEngine
pause