use std::time::{SystemTime};

fn binary_search(arr: [i32; 25], target: i32) -> i32 {
    let mut left = 0;
    let mut right = arr.len() - 1;
    while left <= right {
        let mid = left + (right - left) / 2;
        if arr[mid] == target {
            return mid as i32;
        }
        if arr[mid] < target {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    -1
}

fn main() {
    let before = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();

    let arr: [i32; 25] = [2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834];

    binary_search(arr, arr[2]);
    binary_search(arr, arr[8]);
    binary_search(arr, arr[12]);
    binary_search(arr, arr[15]);
    binary_search(arr, arr[20]);

    let after = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();
    println!("{}", after.as_nanos() - before.as_nanos());
}