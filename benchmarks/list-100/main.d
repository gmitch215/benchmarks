#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;
import std.array : appender;

void main()
{
    auto sw = StopWatch(AutoStart.yes);

    int[] list;
    auto app = appender(&list);

    foreach (i; 0 .. 100)
        app ~= i;

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
