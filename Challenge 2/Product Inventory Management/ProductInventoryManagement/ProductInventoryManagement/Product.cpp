#include <iostream>
#include <string>
#include <string.h>
using namespace std;

class Product
{
public:
	Product();
	~Product();

	Product(std::string name, double price, long quantity);
	std::string getName() {
		return name;
	}

	double getPrice() {
		return price;
	}

	double getQuantity() {
		return quantity;
	}

	void print() {
		cout << "Name: " << name << ", Price: " << price << ", Quantity: " << quantity << endl;
	}

private:
	std::string name = "";
	double price = 0.0;
	int quantity = 0;
};

Product::Product()
{
	this->name = "";
	this->price = 0.0;
	this->quantity = 0;
}

Product::Product(std::string name, double price, long quantity) :
	name(name), price(price), quantity(quantity) {}

Product::~Product()
{
}

double inventoryValue(Product products[], int size) {
	if (size <= 0)
	{
		return 0.0;
	}

	double value = 0.0;
	for (int i = 0; i < size; i++)
	{
		value += products[i].getPrice() * products[i].getQuantity() * 1.0;
	}
	return value;
}

std::string highestValueName(Product products[], int size) {

	if (size<=0)
	{
		return 0;
	}
	if (size == 1)
	{
		return products[0].getName();
	}

	int highestPriceIndex = 0;
	for (int i = 1; i < size; i++)
	{
		if (products[i].getPrice() > products[highestPriceIndex].getPrice())
		{
			highestPriceIndex = i;
		}
	}

	return products[highestPriceIndex].getName();
}

bool isAvailable(std::string key, Product products[], int size) {

	if (size ==0)
	{
		return false;
	}

	for (int i = 0; i < size; i++)
	{
		if (products[i].getName() == key)
		{
			if (products[i].getQuantity() > 0)
			{
				return true;
			}
			else {
				return false;
			}
		}
	}
	return false;
}

void sort() {
}
enum class SortAttribute {
	Name,
	Price,
	Quantity
};

enum class SortOrder {
	Ascending,
	Descending
};

int comparer(Product a, Product b, SortAttribute attribute, SortOrder order) {
	int res = 0;
	switch (attribute)
	{
	case SortAttribute::Name: 
		if (a.getName() == b.getName()) {
			return 0;
		}
		res = a.getName() < b.getName() ? 1 : -1; // 1 = true, -1 = false, 0 = neutral
		break;

	case SortAttribute::Price:
		if (a.getPrice() == b.getPrice()) {
			return 0;
		}
		res = a.getPrice() < b.getPrice() ? 1 : -1;
		break;

	case SortAttribute::Quantity:
		if (a.getQuantity() == b.getQuantity())
		{
			return 0;
		}
		res = a.getQuantity() < b.getQuantity()? 1: -1;
		break;

	default:
		return 0;
	}
	if (order == SortOrder::Ascending) return res;
	return res == 1 ? -1 : 1;

}

int partition(Product products[], int low, int high, SortAttribute att, SortOrder order) {

	Product pivotProd = products[low];
	int i = low + 1;
	int j = high;

	while (i <= j) {
		while (i <= high && comparer(products[i], pivotProd, att, order) == 1) i++;
		while (j >= low && comparer(pivotProd, products[j], att, order) == 1) j--;
		if (i < j)
		{
			std::swap(products[i], products[j]);
		}
	}
	std::swap(products[low], products[j]);
	return j;
}

void quickSort(Product products[], int low, int high, SortAttribute att, SortOrder ord) {
	if (low < high)
	{
		int pivotIndex = partition(products, low, high, att, ord);
		quickSort(products, low, pivotIndex - 1, att, ord);
		quickSort(products, pivotIndex + 1, high, att, ord);
	}
}

int main() {
	//Sample data
	Product products[] = {
		Product("Laptop", 1499.99, 10),
		Product("Smartphone", 799.49, 50),
		Product("Tablet", 599.95, 30),
		Product("Smartwatch", 249.99, 40),
		Product("Headphones", 119.75, 100)
	};

	int size = sizeof(products) / sizeof(products[0]);
	cout << "Size: " << size << endl;

	SortAttribute att = SortAttribute::Quantity;
	SortOrder ord = SortOrder::Ascending;

	quickSort(products, 0, size - 1, att, ord);

	cout << "Inventory Value: " << inventoryValue(products, size) << endl << endl;
	cout << "Highest Price: " << highestValueName(products, size) << endl << endl;

	cout << "Have Headphones: ";
	isAvailable("Headphones", products, size) ? cout << "True" : cout << "False";
	cout << endl << endl;

	cout << "Have Apple: ";
	isAvailable("Apple", products, size) ? cout << "True" : cout << "False";
	cout << endl << endl;

	cout << "Sorted: \n";
	for (Product product : products)
	{
		product.print();
	}

	system("pause");
	return 0;
}