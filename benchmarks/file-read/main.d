#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;
import std.file : read;

void main()
{
    auto sw = StopWatch(AutoStart.yes);

    read("file.txt");

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
