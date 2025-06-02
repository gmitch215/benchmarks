#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;

ulong next(ulong n)
{
    if (n % 2 == 0)
        return n / 2;
    else
        return 3 * n + 1;
}

void main()
{
    auto sw = StopWatch(AutoStart.yes);

    ulong n = 837_799;
    while (n != 1)
        n = next(n);

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
