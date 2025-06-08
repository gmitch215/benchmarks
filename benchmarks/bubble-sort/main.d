#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;
import std.algorithm.mutation : swap;

int[] bubbleSort(int[] arr)
{
    foreach (i; 0 .. arr.length)
        foreach (j; 0 .. arr.length - i)
            if (arr[j] > arr[j + 1])
                swap(arr[j], arr[j + 1]);
    return arr;
}

void main()
{
    auto sw = StopWatch(AutoStart.yes);

    int[] arr = [
        639, 575, 652, 103, 262, 571, 542, 11, 278, 144, 885, 300, 897, 303, 726,
        272, 214, 346, 935, 409, 76, 877, 367, 837, 728, 761, 205, 143, 658, 908,
        263, 388, 317, 159, 331, 107, 127, 635, 972, 773, 81, 964, 302, 294, 82, 7,
        671, 507, 802, 966
    ];
    bubbleSort(arr);

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
