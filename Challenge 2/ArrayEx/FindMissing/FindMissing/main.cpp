#include <iostream>
using namespace std;

long findMissingNumber(long arr[], int size) {
	if (arr == nullptr) return -1;
	long sum = 0;
	for (int i = 0; i < size; i++) {
		sum += arr[i];
	}

	long actualSum = (size + 1) * (size + 1 + 1) / 2;
	return actualSum - sum;
}

int main() {
	long sampleArray[] = { 3 ,7 ,1, 2, 6, 4 };// missing 5
	int size = sizeof(sampleArray) / sizeof(sampleArray[0]);
	cout << findMissingNumber(sampleArray, size);
}