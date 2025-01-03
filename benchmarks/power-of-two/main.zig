const std = @import("std");

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    const one: u64 = 1;
    var n: u64 = 0;
    for (0..31) |i| {
        n = one << @intCast(i);
    }

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}