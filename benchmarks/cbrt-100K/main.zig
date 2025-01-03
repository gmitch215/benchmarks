const std = @import("std");

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    for (0..99999) |i| {
        const j = @as(f32, @floatFromInt(i));
        _ = std.math.cbrt(j);
    }

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}