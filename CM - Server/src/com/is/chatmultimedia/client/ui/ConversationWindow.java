package com.is.chatmultimedia.client.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ConversationWindow extends JFrame {

  private JTabbedPane tabbedPanel;
  private ConversationsListener conversationListener;

  private static final int DEFAULT_WIDTH = 400;
  private static final int DEFAULT_HEIGHT = 300;
  private static final long serialVersionUID = 1;

  private static final String APP_ICON_16 = "resources//chat logo 16x16.png";
  
  public ConversationWindow(String title, ConversationPanel firstPanel) {
    tabbedPanel = new JTabbedPane();
    addConversationPanel(title, firstPanel);

    this.setContentPane(tabbedPanel);
    this.setIconImage(new ImageIcon(APP_ICON_16).getImage());
    this.addWindowListener(new WindowClosing());
    this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    this.setTitle(tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex()));
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }

  public void addConversationPanel(String title, ConversationPanel panel) {
    tabbedPanel.add(title, panel);
    tabbedPanel.setSelectedIndex(tabbedPanel.getTabCount() - 1);
    tabbedPanel.setTabComponentAt(tabbedPanel.getSelectedIndex(), new TabPanel(this, tabbedPanel, title, panel));
    this.setTitle(title);
  }
  
  public void setSelectedTab(ConversationPanel panel) {
    tabbedPanel.setSelectedComponent(panel);
  }

  public void registerConversationListener(ConversationsListener conversationsListener) {
    this.conversationListener = conversationsListener;
  }

  public interface ConversationsListener {

    public void conversationClosed(String identifier);

    public void conversationWindowClosed();

  }
  
  private void closeWindow() {
    this.setVisible(false);
    this.dispose();
    conversationListener.conversationWindowClosed();
  }

  private class TabPanel extends JPanel {

    private JLabel title;
    private JLabel closeButton;

    private static final long serialVersionUID = 1;

    public TabPanel(final JFrame window, final JTabbedPane parentPanel, String tabTitle,
        final ConversationPanel addedPanel) {
      this.setLayout(new GridBagLayout());
      GridBagConstraints constraints = new GridBagConstraints();

      this.setOpaque(false);

      title = new JLabel(tabTitle);
      constraints.anchor = GridBagConstraints.WEST;
      constraints.gridx = 0;
      constraints.gridy = 0;
      constraints.weightx = 1;
      constraints.weighty = 1;
      constraints.fill = GridBagConstraints.HORIZONTAL;
      this.add(title, constraints);

      closeButton = new JLabel("x");
      closeButton.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent event) {
          parentPanel.remove(addedPanel);
          conversationListener.conversationClosed(addedPanel.getIdentifier());
          if (tabbedPanel.getTabCount() == 0) {
            closeWindow();
          }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
          closeButton.setForeground(Color.RED);
        }

        @Override
        public void mouseExited(MouseEvent e) {
          closeButton.setForeground(Color.GRAY);
        }
      });
      closeButton.setForeground(Color.GRAY);
      constraints.anchor = GridBagConstraints.EAST;
      constraints.gridx = 1;
      constraints.gridy = 0;
      constraints.weightx = 0;
      constraints.weighty = 0;
      constraints.fill = GridBagConstraints.NONE;
      this.add(new JLabel("  "), constraints);
      constraints.gridx = 2;
      this.add(closeButton, constraints);
    }

  }

  private class WindowClosing extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      closeWindow();
    }
  }

}
