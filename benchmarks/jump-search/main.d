#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;
import std.algorithm : min;
import std.math : sqrt;

int jumpSearch(int[25] arr, int target)
{
    auto n = arr.length;
    if (n == 0)
        return -1;

    auto blockSize = cast(ulong) sqrt(cast(float) n);
    ulong prev = 0;
    auto curr = blockSize;

    while (prev < n && arr[min(curr, n) - 1] < target)
    {
        prev = curr;
        curr += blockSize;
        if (prev >= n)
            return -1;
    }

    foreach (i; prev .. min(curr, n))
        if (arr[i] == target)
            return cast(int) i;

    return -1;
}

void main()
{
    int[25] arr = [
        2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571,
        612, 634, 678, 712, 745, 789, 812, 834
    ];
    auto sw = StopWatch(AutoStart.yes);

    jumpSearch(arr, arr[2]);
    jumpSearch(arr, arr[8]);
    jumpSearch(arr, arr[12]);
    jumpSearch(arr, arr[15]);
    jumpSearch(arr, arr[20]);

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
