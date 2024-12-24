use std::time::{SystemTime};

fn bubble_sort(array: Vec<i32>) -> Vec<i32> {
    let mut array = array;
    let arr_len = array.len();
    for i in 0..arr_len - 1{
        for j in 0..arr_len - 1 - i {
            if array[j] > array[j + 1] {
                array.swap(j, j + 1);
            }
        }
    }

    array
}

fn main() {
    let before = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();

    let arr = vec![639, 575, 652, 103, 262, 571, 542, 11, 278, 144, 885, 300, 897, 303, 726, 272, 214, 346, 935, 409, 76, 877, 367, 837, 728, 761, 205, 143, 658, 908, 263, 388, 317, 159, 331, 107, 127, 635, 972, 773, 81, 964, 302, 294, 82, 7, 671, 507, 802, 966];
    let _ = bubble_sort(arr);

    let after = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();
    println!("{}", after.as_nanos() - before.as_nanos());
}