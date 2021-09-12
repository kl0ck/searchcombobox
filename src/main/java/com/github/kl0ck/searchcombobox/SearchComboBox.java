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
	
	private boolean allowFreeText = false;

	public SearchComboBox() {
		originalModel = new DefaultComboBoxModel<>();
		super.setModel(originalModel);
		textField().addKeyListener(new TextFieldKeyListener());
	}

	private class TextFieldKeyListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();
			
			// Ignora teclas que não geram caracteres, para não afetar a lista de resultados.
			if (e.isActionKey() || e.isAltDown() || e.isAltGraphDown() || e.isControlDown() || e.isMetaDown() || e.isShiftDown()) {
				return;
			} else if (KeyEvent.VK_DELETE == keyCode || KeyEvent.VK_ESCAPE == keyCode || KeyEvent.VK_ENTER == keyCode) {
				return;
			}
			
			String text = textField().getText();
			
			List<ComboItem<K, V>> results = search(text);
			
			showSearchResults(text, results);
		}
	}

	protected List<ComboItem<K, V>> search(String text) {
		//System.out.println("search = " + text);
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
			// Itens filtrados conforme o texto digitado.
			DefaultComboBoxModel<ComboItem<K,V>> newModel = new DefaultComboBoxModel<>(new Vector<>(results));
			setModel(newModel);
			
		} else {
			// Itens originais, ou seja, todos os itens da lista.
			setModel(originalModel);
		}

		textField().setText(text);
		showPopup();
	}
	
	public boolean allowFreeText() {
		return allowFreeText;
	}

	public void setAllowFreeText(boolean allowFreeText) {
		this.allowFreeText = allowFreeText;
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
		combo.addItem(ComboItem.build(5, "Reino Unido"));
		combo.addItem(ComboItem.build(6, "Países Baixos"));
		combo.setEditable(true);
		
		JOptionPane.showMessageDialog(null, combo);
		
		System.out.println(String.format("Item selecionado: %s[%s]", combo.getSelectedItem().getClass().getSimpleName(), combo.getSelectedItem()));
		System.out.println("Texto: " + combo.getTextField().getText());
	}

}
