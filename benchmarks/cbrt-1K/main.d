#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;
import core.stdc.math : cbrt;

void main()
{
    auto sw = StopWatch(AutoStart.yes);

    foreach (i; 0.0 .. 1_000.0)
        cbrt(i);

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
