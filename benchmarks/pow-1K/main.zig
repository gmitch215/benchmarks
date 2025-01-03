const std = @import("std");

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    for (0..999) |i| {
        const y = 1.1 + (@as(f32, @floatFromInt(i)) / 1000.0);
        _ = std.math.pow(f64, 2.0, y);
    }

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}