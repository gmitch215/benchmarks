#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;
import std.math : sqrt;

bool isPrime(ulong n)
{
    auto start = 2;
    auto limit = sqrt(cast(float) n);
    while (start <= cast(ulong) limit)
    {
        if (n % start == 0)
            return false;
        start++;
    }

    return true;
}

void main()
{
    auto sw = StopWatch(AutoStart.yes);

    foreach (i; 0 .. 100_000)
        isPrime(i);

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
