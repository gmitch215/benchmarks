#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
version (Windows)
    pragma(lib, "bcrypt");

import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;
import std.random : rndGen, uniform;

void main()
{
    auto sw = StopWatch(AutoStart.yes);

    float n = 0.0;
    foreach (i; 0 .. 1_000)
    {
        n = uniform(0.0, 1.0, rndGen);
    }
    n = n;

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}