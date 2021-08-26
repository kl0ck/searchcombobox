package com.github.kl0ck.searchcombobox;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class SearchComboBox<K, V> extends JComboBox<ComboItem<K, V>> {
	
	private final DefaultComboBoxModel<ComboItem<K,V>> originalModel;

	public SearchComboBox() {
		originalModel = new DefaultComboBoxModel<>();
		super.setModel(originalModel);
		
		textField().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String text = textField().getText();
				List<ComboItem<K, V>> results = search(text);
				System.out.println(results);
				showSearchResults(text, results);
			}
		});
	}

	private List<ComboItem<K, V>> search(String text) {
		System.out.println("search = " + text);
		if (text == null || text.trim().isEmpty()) {
			return Collections.emptyList();
		}
		
		List<ComboItem<K, V>> results = new ArrayList<>();
		
		for (int i = 0; i < originalModel.getSize(); i++) {
			ComboItem<K, V> item = originalModel.getElementAt(i);
			
			if (item.getValue() != null && text != null && item.getValue().toString().toLowerCase().contains(text.toLowerCase())) {
				results.add(item);
			}
		}
		
		return results;
	}
	
	protected void showSearchResults(String text, List<ComboItem<K, V>> results) {
		if (!results.isEmpty()) {
			// Mostra itens conforme o texto digitado
			DefaultComboBoxModel<ComboItem<K,V>> newModel = new DefaultComboBoxModel<>(new Vector<>(results));
			setModel(newModel);
			
		} else {
			// Mostra todos os itens
			setModel(originalModel);
		}

		textField().setText(text);
		showPopup();
	}

	public JTextField textField() {
		return (JTextField) getEditor().getEditorComponent();
	}

	public JTextField getTextField() {
		return textField();
	}

	public static void main(String[] args) {
		SearchComboBox<Integer, String> combo = new SearchComboBox<>();
		combo.addItem(ComboItem.build());
		combo.addItem(ComboItem.build(1, "Argentina"));
		combo.addItem(ComboItem.build(2, "Brasil"));
		combo.addItem(ComboItem.build(3, "Canadá"));
		combo.addItem(ComboItem.build(4, "Dinamarca"));
		combo.setEditable(true);
		
		JOptionPane.showMessageDialog(null, combo);
		
		System.out.println(String.format("Item selecionado: %s [%s]", combo.getSelectedItem().getClass().getSimpleName(), combo.getSelectedItem()));
		System.out.println("Texto: " + combo.getTextField().getText());
	}

}
