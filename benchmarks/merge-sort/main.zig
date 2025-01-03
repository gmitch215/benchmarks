const std = @import("std");

fn merge(array: []i32, left: usize, mid: usize, right: usize) !void {
    const allocator = std.heap.page_allocator;

    var i = left;
    var j = mid + 1;

    var temp = try allocator.alloc(i32, right - left + 1);
    defer allocator.free(temp);

    var k: usize = 0;

    while (i <= mid and j <= right) {
        if (array[i] <= array[j]) {
            temp[k] = array[i];
            i += 1;
        } else {
            temp[k] = array[j];
            j += 1;
        }
        k += 1;
    }

    while (i <= mid) {
        temp[k] = array[i];
        i += 1;
        k += 1;
    }

    while (j <= right) {
        temp[k] = array[j];
        j += 1;
        k += 1;
    }

    for (0..temp.len) |l| {
        array[left + l] = temp[l];
    }
}

fn merge_sort(array: []i32, left: usize, right: usize) !void {
    if (left < right) {
        const mid = left + (right - left) / 2;

        try merge_sort(array, left, mid);
        try merge_sort(array, mid + 1, right);
        try merge(array, left, mid, right);
    }
}

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    var arr = [_]i32{312, 661, 153, 887, 650, 11, 44, 42, 159, 603, 674, 811, 290, 333, 794, 242, 875, 372, 671, 924, 782, 5, 617, 966, 390, 748, 876, 823, 236, 974, 880, 331, 727, 631, 944, 769, 19, 983, 666, 844, 301, 546, 129, 299, 814, 412, 406, 1000, 689, 984};
    try merge_sort(&arr, 0, arr.len - 1);

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}