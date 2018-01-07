import com.google.common.base.Strings;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class DropFrame extends JPanel implements ActionListener {
    private final String artistFieldString = "Artist";
    private final String titleFieldString = "Song Title";
    private JTextField artistField;
    private JTextField titleField;
    private JEditorPane mp3InfoPane;
    private JFrame frame;
    private File file;

    public DropFrame() {
        //text field for artist
        artistField = new JTextField(20);
        artistField.setActionCommand(artistFieldString);
        artistField.addActionListener(this);

        titleField = new JTextField(20);
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
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

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

    public void actionPerformed(ActionEvent e) {
        try {
//            if (artistFieldString.equals(e.getActionCommand())) {
//                JTextField source = (JTextField) e.getSource();
//                id3v2Tag.setArtist(source.getText());
//            } else if (titleFieldString.equals(e.getActionCommand())) {
//                JTextField source = (JTextField) e.getSource();
//                id3v2Tag.setTitle(source.getText());
//            }

            //if artist and title field are filled
            if (!Strings.isNullOrEmpty(artistField.getText()) &&
                    Strings.isNullOrEmpty(titleField.getText())){
                Mp3File mp3file = new Mp3File(file.getAbsolutePath());
                ID3v2 id3v2Tag;
                if (mp3file.hasId3v2Tag()) {
                    id3v2Tag = mp3file.getId3v2Tag();
                } else {
                    // mp3 does not have an ID3v2 tag, let's create one..
                    id3v2Tag = new ID3v24Tag();
                    mp3file.setId3v2Tag(id3v2Tag);
                }

                id3v2Tag.setArtist(artistField.getText());
                id3v2Tag.setTitle(titleField.getText());

                mp3file.save(file.getAbsolutePath());
            }
            
        } catch (Exception ex) {
            System.out.println("Error: No File Found!");
        }
    }

    private void displayFiles() {
        new FileDrop(System.out, mp3InfoPane, new FileDrop.Listener() {
            public void filesDropped(java.io.File[] files) {
                for (int i = 0; i < files.length; i++) {
                    try {
                        file = files[i];
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
