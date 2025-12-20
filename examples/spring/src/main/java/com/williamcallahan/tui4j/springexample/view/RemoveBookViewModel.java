package com.williamcallahan.tui4j.springexample.view;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.UpdateResult;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Position;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.Style;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.border.StandardBorder;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.color.Color;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.join.VerticalJoinDecorator;
import com.williamcallahan.tui4j.compat.bubbletea.lipgloss.placement.PlacementDecorator;
import com.williamcallahan.tui4j.compat.bubbletea.message.KeyPressMessage;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.help.Help;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.help.KeyMap;
import com.williamcallahan.tui4j.compat.bubbletea.bubbles.key.Binding;
import com.williamcallahan.tui4j.springexample.model.Book;
import com.williamcallahan.tui4j.springexample.repository.BookRepository;
import org.springframework.stereotype.Component;

/**
 * Example program for remove book view model.
 * tui4j: examples/spring/src/main/java/com/williamcallahan/tui4j/springexample/view/RemoveBookViewModel.java
 */
@Component
public class RemoveBookViewModel implements Model, KeyMap {

    private final BookRepository bookRepository;
    private Model previousModel;

    private Book book;
    private int width;
    private int height;

    private final Style warningBoxStyle;
    private final Style warningMessageStyle;
    private final Binding confirm;
    private final Binding back;
    private Help help;

    public RemoveBookViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.help = new Help();

        this.warningBoxStyle = Style.newStyle()
                .width(10)
                .padding(5)
                .margin(3)
                .border(StandardBorder.RoundedBorder)
                .borderBackground(Color.color("#ff0000"))
                .background(Color.color("#ff0000"));
        this.warningMessageStyle = Style.newStyle()
                .foreground(Color.color("#fff"))
                .blink(true);

        this.confirm = new Binding(Binding.withKeys("y"), Binding.withHelp("y", "confirm"));
        this.back = new Binding(Binding.withKeys("esc"), Binding.withHelp("esc", "go back"));
    }

    public Model prepare(Model previousModel, Book book, int width, int height) {
        this.previousModel = previousModel;
        this.book = book;
        this.width = width;
        this.height = height;
        return this;
    }

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, back)) {
                return UpdateResult.from(previousModel);
            } else if (Binding.matches(keyPressMessage, confirm)) {
                return UpdateResult.from(this, removeBook());
            }
        }
        if (msg instanceof BookRemovedMessage) {
            return previousModel.update(msg);
        }
        return UpdateResult.from(this);
    }

    private Command removeBook() {
        return () -> {
            Long bookId = book.getId();
            if (bookId != null) {
                bookRepository.deleteById(bookId);
            }
            return new BookRemovedMessage();
        };
    }

    @Override
    public String view() {
        return PlacementDecorator.place(
                width,
                height,
                Position.Center,
                Position.Center,
                VerticalJoinDecorator.joinVertical(
                        Position.Center,
                        warningBoxStyle.render(
                                VerticalJoinDecorator.joinVertical(
                                        Position.Center,
                                        warningMessageStyle.render("Are you sure you want to remove this book?"),
                                        "",
                                        "“%s”".formatted(book.getTitle())
                                )
                        ),
                        help.render(this)
                )
        );
    }

    @Override
    public Binding[] shortHelp() {
        return new Binding[]{
                confirm,
                back
        };
    }

    @Override
    public Binding[][] fullHelp() {
        return new Binding[][]{
                new Binding[]{
                        confirm, back
                }
        };
    }
}
