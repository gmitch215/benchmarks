#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;
import std.random : random;

void main()
{
    auto sw = StopWatch(AutoStart.yes);

    float n = 0.0;
    foreach (i; 0 .. 1_000_000)
    {
        n = random();
    }
    n = n;

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
