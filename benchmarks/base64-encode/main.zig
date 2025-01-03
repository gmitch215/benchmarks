const std = @import("std");

pub fn main() !void {
    const str1 = "Hello, World!";
    const str2 = "The quick brown fox jumps over the lazy dog.";
    const str3 = "AaBbCcDdEeFf123456_!@#&*^()$=+-";

    const before: i128 = std.time.nanoTimestamp();

    var gpa = std.heap.GeneralPurposeAllocator(.{}){};
    defer _ = gpa.deinit();
    const allocator = gpa.allocator();

    const l1 = std.base64.standard.Encoder.calcSize(str1.len);
    const b1 = try allocator.alloc(u8, l1);
    defer allocator.free(b1);
    _ = std.base64.standard.Encoder.encode(b1, str1);

    const l2 = std.base64.standard.Encoder.calcSize(str2.len);
    const b2 = try allocator.alloc(u8, l2);
    defer allocator.free(b2);
    _ = std.base64.standard.Encoder.encode(b2, str2);

    const l3 = std.base64.standard.Encoder.calcSize(str3.len);
    const b3 = try allocator.alloc(u8, l3);
    defer allocator.free(b3);
    _ = std.base64.standard.Encoder.encode(b3, str3);

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}