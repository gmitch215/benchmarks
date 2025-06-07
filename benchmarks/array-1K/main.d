#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;

void main()
{
    auto sw = StopWatch(AutoStart.yes);

    enum N = 1_000;
    int[N] arr;
    foreach (i; 0 .. N)
        arr[i] = i;

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
