#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;

void main()
{
    auto sw = StopWatch(AutoStart.yes);

    int n;
    foreach(i; 0..30)
        n = 1 << i;

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
