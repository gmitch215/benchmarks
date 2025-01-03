const std = @import("std");

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    var arr: [100000]u32 = undefined;
    for (0..arr.len) |i| {
        arr[i] = @intCast(i);
    }

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}