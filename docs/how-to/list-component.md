# List Component

The `List` component displays filterable, paginated lists in the terminal. It supports both static arrays and dynamic data sources.

## Quick Start

```java
import com.williamcallahan.tui4j.compat.bubbles.list.*;

// Define items using a record
record ProductItem(String title, String description) implements DefaultItem {
    @Override
    public String filterValue() {
        return title();
    }
}

// Create the list
Item[] items = {
    new ProductItem("Laptop", "High-performance laptop"),
    new ProductItem("Smartphone", "Latest model smartphone"),
    new ProductItem("Headphones", "Noise-canceling headphones")
};

List list = new List(items, 40, 10);  // width=40, height=10
```

## Constructors

| Constructor | Description |
|-------------|-------------|
| `List(Item[], int width, int height)` | Static items with DefaultDelegate |
| `List(Item[], ItemDelegate, int width, int height)` | Static items with custom delegate |
| `List(ListDataSource, int width, int height)` | Dynamic source with DefaultDelegate |
| `List(ListDataSource, ItemDelegate, int width, int height)` | Dynamic source with custom delegate |

## Item Interfaces

```
Item                    DefaultItem extends Item
├── filterValue()       ├── filterValue()
                        ├── title()
                        └── description()
```

- **`Item`** — Base interface; only requires `filterValue()` for filtering
- **`DefaultItem`** — Extends `Item`; adds `title()` and `description()` for the default renderer

## Dynamic Data Sources

For large datasets (e.g., database-backed), implement `ListDataSource` instead of passing arrays:

```java
import com.williamcallahan.tui4j.compat.bubbles.list.*;

public class ProductDataSource implements ListDataSource {

    private final ProductRepository repository;

    public ProductDataSource(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public FetchedItems fetchItems(int page, int perPage, String filterValue) {
        var pageRequest = PageRequest.of(page, perPage);

        var products = (filterValue == null || filterValue.isEmpty())
            ? repository.findAll(pageRequest).getContent()
            : repository.findByNameContaining(filterValue, pageRequest);

        var filteredItems = products.stream()
            .map(p -> new ProductItem(p.getName(), p.getDescription()))
            .map(FilteredItem::new)
            .toList();

        long total = repository.count();
        long matchedItems = (filterValue == null || filterValue.isEmpty())
            ? total
            : repository.countByNameContaining(filterValue); // replace with your filtered count
        int totalPages = (int) Math.ceil((double) total / perPage);

        return new FetchedItems(filteredItems, matchedItems, total, totalPages);
    }
}

// Usage
List list = new List(new ProductDataSource(repo), 40, 10);
```

## Custom Delegates

To customize item rendering, implement `ItemDelegate` instead of using `DefaultDelegate`:

```java
public class CustomDelegate implements ItemDelegate {
    @Override
    public String render(Item item, int index, boolean isSelected, int width) {
        String prefix = isSelected ? "> " : "  ";
        return prefix + item.filterValue();
    }

    @Override
    public int height() {
        return 1;  // Lines per item
    }

    @Override
    public int spacing() {
        return 0;  // Lines between items
    }
}
```

## Configuration

```java
list.setTitle("Products");
list.setShowTitle(true);
list.setShowFilter(true);
list.setShowStatusBar(true);
list.setShowPagination(true);
list.setShowHelp(true);
list.setFilteringEnabled(true);
list.setStatusMessageLifetime(Duration.ofSeconds(5));
```

## Examples

See working examples in the repository:
- [`listdefault/`](../../src/main/resources/examples/compat/bubbletea/listdefault/) — Basic list with DefaultDelegate
- [`listsimple/`](../../src/main/resources/examples/compat/bubbletea/listsimple/) — Minimal list setup
- [`listfancy/`](../../src/main/resources/examples/compat/bubbletea/listfancy/) — Styled list with custom delegate
