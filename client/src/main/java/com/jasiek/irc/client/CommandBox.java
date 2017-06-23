package com.jasiek.irc.client;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.util.function.Consumer;

public class CommandBox extends TextBox {

    private Consumer<TextBox> onEnterPressed;

    public CommandBox(TerminalSize terminalSize) {
        super(terminalSize);
    }

    @Override
    public synchronized Result handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Enter) {
            onEnterPressed.accept(this);
            return Result.HANDLED;
        } else {
            return super.handleKeyStroke(keyStroke);
        }
    }

    public Consumer<TextBox> getOnEnterPressed() {
        return onEnterPressed;
    }

    public void setOnEnterPressed(Consumer<TextBox> onEnterPressed) {
        this.onEnterPressed = onEnterPressed;
    }
}
