package com.jasiek.irc.client;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.jasiek.irc.common.network.message.messages.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class GUI {

    private final int width = 80;

    private final int height = 20;

    private Client client;

    private TextBox chatBox;

    private CommandBox commandBox;

    private MultiWindowTextGUI gui;

    public GUI() {
    }

    public void create() throws IOException {
        // Setup terminal and screen layers
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        // Create gui and start gui
        gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        gui.addWindowAndWait(createIPWindow());
    }

    public BasicWindow createMainWindow() {
        Panel panel = new Panel();
        panel.setPreferredSize(new TerminalSize(width, height));
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        createComponents(panel);

        // Create window to hold the panel
        BasicWindow window = new BasicWindow();
        window.setHints(Arrays.asList(Window.Hint.FULL_SCREEN));
        window.setComponent(panel);

        window.setFocusedInteractable(commandBox);

        return window;
    }

    public BasicWindow createIPWindow() {
        BasicWindow window = new BasicWindow();
        window.setHints(Arrays.asList(Window.Hint.CENTERED));

        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        {
            Label label = new Label("Insert IP and port");
            label.addTo(panel);

            TextBox box = new TextBox(new TerminalSize(20, 1));
            panel.addComponent(box.withBorder(Borders.singleLine("IP:port")));

            Button button = new Button("Connect", () -> {
                // if correct
                URI url = parseUrl(box.getText());
                if (url != null) {
                    //System.out.println(url.getHost());
                    //System.out.println(url.getPort());
                    this.client = new Client(this, url.getHost(), url.getPort());
                    if (client.run()) {
                        window.close();
                        gui.addWindowAndWait(createMainWindow());
                        client.disconnect();
                    } else {
                        MessageDialog.showMessageDialog(gui, "Error", "Can't reach server");
                    }
                }
            });
            button.addTo(panel);
        }

        window.setComponent(panel);
        return window;
    }

    public URI parseUrl(String string) {
        String host;
        int port;

        try {
            // WORKAROUND: add any scheme to make the resulting URI valid.
            URI uri = new URI("my://" + string); // may throw URISyntaxException
            host = uri.getHost();
            port = uri.getPort();

            if (uri.getHost() == null || uri.getPort() == -1) {
                throw new URISyntaxException(uri.toString(),
                        "URI must have host and port parts");
            }

            return uri;
        } catch (URISyntaxException ex) {
            // validation failed
        }

        return null;
    }

    public void createComponents(Panel panel) {
        chatBox = new TextBox(new TerminalSize(width, height - 1));
        chatBox.setReadOnly(true);
        chatBox.setText("Welcome to the Internet Relay Chat made by Jan Waśko! Supported commands:\n" +
                "NICK [nick]\n" +
                "JOIN [channel]\n" +
                "PART [channel]\n" +
                "SEND [channel] [message]");
        panel.addComponent(chatBox.withBorder(Borders.singleLine("Chat")));

        commandBox = new CommandBox(new TerminalSize(width, 1));
        commandBox.withBorder(Borders.singleLine("Command"));
        commandBox.addTo(panel);
        commandBox.setOnEnterPressed(box -> {
            handleCommand(box.getText());
            box.setText("");
        });
    }

    private void handleCommand(String text) {
        client.handleInput(text);
    }

    public void handleConfirmSetNick(ConfirmSetNick confirmSetNick) {
        displayMessage("*** Your nick is " + confirmSetNick.getNick());
    }

    public void handleConfirmJoinChannel(ConfirmJoinChannel confirmJoinChannel) {
        //displayMessage("*** Now talking in " + confirmJoinChannel.getChannelName());
        displayMessage("*** " + confirmJoinChannel.getNick() + " has joined the channel " + confirmJoinChannel.getChannelName());
    }

    public void handleConfirmPartChannel(ConfirmPartChannel confirmPartChannel) {
        //displayMessage("*** Now leaving " + confirmPartChannel.getChannelName());
        displayMessage("*** " + confirmPartChannel.getNick() + " has left the channel " + confirmPartChannel.getChannelName());
    }

    public void handleConfirmChannelMessage(ConfirmChannelMessage confirmChannelMessage) {
        displayMessage("[" + confirmChannelMessage.getChannelName() + "] " + confirmChannelMessage.getNick() + ": " + confirmChannelMessage.getText());
    }

    public void handleErrorMessage(ErrorMessage errorMessage) {
        displayMessage("*** ERROR: " + errorMessage.getText());
    }

    public void displayMessage(String text) {
        chatBox.setText(chatBox.getText() + "\n" + text);
        chatBox.setCaretPosition(chatBox.getLineCount(), 0);
    }
}
