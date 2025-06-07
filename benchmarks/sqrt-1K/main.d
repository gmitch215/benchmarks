#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;
import std.math : sqrt;

void main()
{
    auto sw = StopWatch(AutoStart.yes);

    foreach (i; 0.0 .. 1_000.0)
    {
        sqrt(i);
    }

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
