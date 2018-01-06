import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DropFrame extends JPanel implements ActionListener {
    private final String artistFieldString = "Artist";
    private final String titleFieldString = "Song Title";
    private JEditorPane mp3InfoPane;
    private JFrame frame;

    public DropFrame(){
        //text field for artist
        JTextField artistField = new JTextField(20);
        artistField.setActionCommand(artistFieldString);
        artistField.addActionListener(this);

        JTextField titleField = new JTextField(20);
        titleField.setActionCommand(titleFieldString);
        titleField.addActionListener(this);

        //creating labels for fields
        JLabel artistFieldLabel = new JLabel(artistFieldString + ":");
        artistFieldLabel.setLabelFor(artistField);
        JLabel titleFieldLabel = new JLabel(titleFieldString + ":");
        titleFieldLabel.setLabelFor(titleField);

        JPanel textControlsPane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        textControlsPane.setLayout(gridbag);

        JLabel[] labels = {artistFieldLabel, titleFieldLabel};
        JTextField[] textFields = {artistField, titleField};
        addLabelTextRows(labels, textFields, gridbag, textControlsPane);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        textControlsPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Text Fields"),
                        BorderFactory.createEmptyBorder(5,5,5,5)));

        //File drop pane
        mp3InfoPane = new JEditorPane("text/html", "");
        JScrollPane areaScrollPane = new JScrollPane(mp3InfoPane);
        mp3InfoPane.setText("<b> DROP FILE HERE </b>");

        JPanel pane = new JPanel(new GridLayout(1, 0));
        pane.add(areaScrollPane, BorderLayout.PAGE_START);
        pane.add(textControlsPane, BorderLayout.PAGE_END);
        add(pane, BorderLayout.LINE_START);
    }

    private void addLabelTextRows(JLabel[] labels,
                                  JTextField[] textFields,
                                  GridBagLayout gridbag,
                                  Container container) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        int numLabels = labels.length;

        for (int i = 0; i < numLabels; i++) {
            c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
            c.fill = GridBagConstraints.NONE;      //reset to default
            c.weightx = 0.0;                       //reset to default
            container.add(labels[i], c);

            c.gridwidth = GridBagConstraints.REMAINDER;     //end row
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            container.add(textFields[i], c);
        }
    }

    public void actionPerformed(ActionEvent e){
        if (artistFieldString.equals(e.getActionCommand())) {
            JTextField source = (JTextField)e.getSource();
        } else if (titleFieldString.equals(e.getActionCommand())) {
            JPasswordField source = (JPasswordField)e.getSource();
        }
    }

    private void displayFiles(){
        new FileDrop(System.out, mp3InfoPane, new FileDrop.Listener() {
            public void filesDropped(java.io.File[] files) {
                for (int i = 0; i < files.length; i++) {
                    try {
                        mp3InfoPane.setText("<b>PATH:      </b>" + files[i].getAbsolutePath() + "<br>" +
                                "<b>FILE NAME: </b>" + files[i].getName());
                    }   // end try
                    catch (Exception e) {
                    }
                }   // end for: through each dropped file
            }   // end filesDropped
        }); // end FileDrop.Listener

        //frame.setBounds(100, 100, 500, 200);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    private String parsePath(String path){
        //path will be in the form file::///home.....
        //we want only /home...., which begins from index 6
        final int index = 6;
        path.replaceAll("%20", " ");
        path = path.substring(index);
        return path;
    }

    public void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("mp3 rename");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(this);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        displayFiles();
    }

}
