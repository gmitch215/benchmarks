const std = @import("std");

pub fn main() !void {
    const str1 = "SGVsbG8sIFdvcmxkIQ==";
    const str2 = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4=";
    const str3 = "QWFCYkNjRGRFZUZmMTIzNDU2XyFAIyYqXigpJD0rLQ==";

    const before: i128 = std.time.nanoTimestamp();

    var gpa = std.heap.GeneralPurposeAllocator(.{}){};
    defer _ = gpa.deinit();
    const allocator = gpa.allocator();

    const l1 = try std.base64.standard.Decoder.calcSizeForSlice(str1);
    const b1 = try allocator.alloc(u8, l1);
    defer allocator.free(b1);
    try std.base64.standard.Decoder.decode(b1, str1);

    const l2 = try std.base64.standard.Decoder.calcSizeForSlice(str2);
    const b2 = try allocator.alloc(u8, l2);
    defer allocator.free(b2);
    try std.base64.standard.Decoder.decode(b2, str2);

    const l3 = try std.base64.standard.Decoder.calcSizeForSlice(str3);
    const b3 = try allocator.alloc(u8, l3);
    defer allocator.free(b3);
    try std.base64.standard.Decoder.decode(b3, str3);

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}