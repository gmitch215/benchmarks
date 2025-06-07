#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;

int linearSearch(int[50] arr, int target)
{
    foreach (i, e; arr)
        if (e == target)
            return cast(int) i;

    return -1;
}

void main()
{
    int[50] arr = [
        1722, 1214, 2169, 1505, 1089, 696, 1706, 1088, 1637, 250, 430, 2466, 1319,
        1933, 1363, 495, 1903, 1750, 2089, 1496, 20, 2262, 483, 1054, 226, 2357,
        2162, 482, 61, 675, 2003, 1914, 1687, 738, 1340, 969, 1683, 1765, 23, 317,
        657, 995, 1112, 2248, 820, 2076, 56, 745, 734, 1884
    ];
    auto sw = StopWatch(AutoStart.yes);

    linearSearch(arr, arr[4]);
    linearSearch(arr, arr[11]);
    linearSearch(arr, arr[29]);
    linearSearch(arr, arr[36]);
    linearSearch(arr, arr[42]);

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
