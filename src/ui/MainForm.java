package ui;

import business.ContactBusiness;
import entity.ContactEntity;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainForm extends JFrame{
    private JPanel rootPanel;
    private JButton buttonNewContact;
    private JButton buttonRemove;
    private JTable tableContext;
    private JLabel labelContactCount;

    private ContactBusiness mContactBusiness;
    private String mNome = "";
    private String mPhone = "";

    public MainForm(){

        setContentPane(rootPanel);
        setSize(500, 250);
        setVisible(true);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mContactBusiness = new ContactBusiness();

        setListeners();

        loadContacts();

    }

    private void setListeners() {
        buttonNewContact.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ContactForm();
                dispose();
            }
        });

        tableContext.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    if(tableContext.getSelectedRow() != -1){
                        mNome = tableContext.getValueAt(tableContext.getSelectedRow(), 0).toString();
                        mPhone = tableContext.getValueAt(tableContext.getSelectedRow(), 1).toString();
                    }
                }
            }
        });

        buttonRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mContactBusiness.delete(mNome, mPhone);

                    loadContacts();

                    mNome = "";
                    mPhone = "";
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
                }
            }
        });
    }

    private void loadContacts() {
        List<ContactEntity> contactList = mContactBusiness.getList();

        String[] columnNames = {"Nome", "Telefone"};
        DefaultTableModel model = new DefaultTableModel(new Object[0][0], columnNames);

        for (ContactEntity i : contactList) {
            Object[] o = new Object[2];

            o[0] = i.getName();
            o[1] = i.getPhone();

            model.addRow(o);
        }

        tableContext.clearSelection();
        tableContext.setModel(model);

        labelContactCount.setText(mContactBusiness.getContactCountDescription());

    }
}
