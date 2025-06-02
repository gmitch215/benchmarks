#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;
import std.array : appender;

int[] mergeSort(int[] arr)
{
    if (arr.length <= 1)
        return arr;

    auto mid = arr.length / 2;
    auto left = mergeSort(arr[0 .. mid]);
    auto right = mergeSort(arr[mid .. $]);

    return merge(left, right);
}

int[] merge(int[] left, int[] right)
{
    int[] result;
    auto app = appender(&result);

    int i, j;

    while (i < left.length && j < right.length)
    {
        if (left[i] < right[j])
            app ~= left[i++];
        else
            app ~= right[j++];
    }

    if (i < left.length)
        while (i < left.length)
            app ~= left[i++];

    if (j < right.length)
        while (j < right.length)
            app ~= right[j++];

    return result;
}

void main()
{
    auto sw = StopWatch(AutoStart.yes);

    int[] arr = [
        312, 661, 153, 887, 650, 11, 44, 42, 159, 603, 674, 811, 290, 333, 794,
        242, 875, 372, 671, 924, 782, 5, 617, 966, 390, 748, 876, 823, 236, 974,
        880, 331, 727, 631, 944, 769, 19, 983, 666, 844, 301, 546, 129, 299, 814,
        412, 406, 1000, 689, 984
    ];
    mergeSort(arr);

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
