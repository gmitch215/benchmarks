const std = @import("std");

fn linear_search(arr: [50]i32, target: i32) i32 {
    var i: i32 = 0;
    while (i < arr.len) : (i += 1) {
        if (arr[@intCast(i)] == target) {
            return i;
        }
    }
    return -1;
}

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    const arr: [50]i32 = .{1722, 1214, 2169, 1505, 1089, 696, 1706, 1088, 1637, 250, 430, 2466, 1319, 1933, 1363, 495, 1903, 1750, 2089, 1496, 20, 2262, 483, 1054, 226, 2357, 2162, 482, 61, 675, 2003, 1914, 1687, 738, 1340, 969, 1683, 1765, 23, 317, 657, 995, 1112, 2248, 820, 2076, 56, 745, 734, 1884};

    _ = linear_search(arr, arr[4]);
    _ = linear_search(arr, arr[11]);
    _ = linear_search(arr, arr[29]);
    _ = linear_search(arr, arr[36]);
    _ = linear_search(arr, arr[42]);

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}