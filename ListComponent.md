# Using the `List` Component in TUI4J

The `List` component in TUI4J is a powerful and flexible way to display lists of items in a terminal UI. You can use it in two different ways:

1. **With a static array of items** – Best for small, predefined lists.
2. **With a dynamic data source** – Recommended for large datasets, such as database-backed lists.

## 1. Using a Static Array of Items

For small lists, you can pass an array of `Item` objects directly. If you're using the default rendering behavior, TUI4J provides `DefaultDelegate`, which requires that each item implements `DefaultItem`.

### Defining a Default Item
To use `DefaultDelegate`, your item must implement `DefaultItem`, which extends `Item` and provides `title()` and `description()` methods.

```java
public class ProductItem implements DefaultItem {
    private final String title;
    private final String description;

    public ProductItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String filterValue() {
        return title;  // Used for filtering
    }
}
```

### Creating a List with a Static Array
If you want to use `DefaultDelegate`, TUI4J provides a constructor that automatically assigns it:

```java
Item[] items = {
    new ProductItem("Laptop", "High-performance laptop"),
    new ProductItem("Smartphone", "Latest model smartphone"),
    new ProductItem("Headphones", "Noise-canceling headphones")
};

// List constructor that automatically assigns DefaultDelegate
List list = new List(items, 40, 10);
```

Alternatively, you can explicitly specify `DefaultDelegate`:

```java
List list = new List(items, new DefaultDelegate(), 40, 10);
```

## 2. Using a Custom `ListDataSource`
For large datasets, you should implement ListDataSource to dynamically fetch and filter data (e.g., from a database).

### Implementing a Custom Data Source

```java
public class ProductDataSource implements ListDataSource {

    private final ProductRepository productRepository;

    public ProductDataSource(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public FetchedItems fetchItems(int page, int perPage, String filterValue) {
        PageRequest pageRequest = PageRequest.of(page, perPage);

        List<Product> products = (filterValue == null || filterValue.isEmpty()) 
            ? productRepository.findAll(pageRequest).getContent()
            : productRepository.findByNameContaining(filterValue, pageRequest);
        
        List<FilteredItem> filteredItems = products.stream()
            .map(product -> new ProductItem(product.getName(), product.getDescription()))
            .map(FilteredItem::new)
            .toList();

        long totalItems = productRepository.count();
        return new FetchedItems(filteredItems, filteredItems.size(), totalItems, (int) Math.ceil((double) totalItems / perPage));
    }

    @Override
    public long totalItems() {
        return productRepository.count();
    }
}
```

### Creating a List with a Dynamic Data Source
Similar to static lists, TUI4J provides a constructor that automatically assigns `DefaultDelegate`:

```java
ListDataSource productDataSource = new ProductDataSource(productRepository);

// List constructor that automatically assigns DefaultDelegate
List list = new List(productDataSource, 40, 10);
```

Or, if you want to explicitly specify a delegate:

```java
List list = new List(productDataSource, new DefaultDelegate(), 40, 10);
```

## DefaultDelegate and DefaultItem
The `DefaultDelegate` class (located in `com.williamcallahan.tui4j.compat.bubbletea.bubbles.list`) requires that all items implement `DefaultItem`.
`DefaultItem` extends `Item` and ensures that the list can properly render titles and descriptions.

```java
public interface DefaultItem extends Item {
    String title();
    String description();
}
```
