#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;

void main()
{
    auto sw = StopWatch(AutoStart.yes);

    foreach (i; 0 .. 10_000)
    {
        auto y = 1.1 + i / 10_000.0;
        2 ^^ y;
    }

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
