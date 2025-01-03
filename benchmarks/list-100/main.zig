const std = @import("std");

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    var gpa = std.heap.GeneralPurposeAllocator(.{}){};
    defer _ = gpa.deinit();

    const allocator = gpa.allocator();

    var list = std.ArrayList(usize).init(allocator);
    defer list.deinit();

    for (1..100) |i| {
        _ = try list.append(i);
    }

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}