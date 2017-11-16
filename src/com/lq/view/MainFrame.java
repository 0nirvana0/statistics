package com.lq.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import com.lq.main.Main;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -526492919273485541L;

	Main start = new Main();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 434, 261);
		getContentPane().add(panel);
		panel.setLayout(null);

		JTextPane filePath = new JTextPane();
		filePath.setBounds(24, 10, 289, 31);
		panel.add(filePath);

		JTextPane outFilePath = new JTextPane();
		outFilePath.setBounds(24, 70, 289, 31);
		panel.add(outFilePath);

		JButton chooseFile = new JButton("选择文件");
		chooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		chooseFile.setBounds(320, 10, 104, 31);
		panel.add(chooseFile);

		JButton chooseOutPath = new JButton("存放目录");
		chooseOutPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fcDlg = new JFileChooser();
				fcDlg.setDialogTitle("请选择文件存放目录");
				fcDlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fcDlg.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String filepath = fcDlg.getSelectedFile().getPath();
					outFilePath.setText(filepath);
				}
			}
		});
		chooseOutPath.setBounds(320, 70, 96, 31);
		panel.add(chooseOutPath);

		JButton NotClose = new JButton("筛选没close");
		NotClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filePathStr = filePath.getText();
				String outFilePathStr = chooseOutPath.getText();
				if (checkPath(filePathStr, outFilePathStr)) {
					start.dealNoClose(filePathStr, outFilePathStr);
				}

			}

		});
		NotClose.setBounds(24, 139, 119, 23);
		panel.add(NotClose);

		JButton newData = new JButton("新的");
		newData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filePathStr = filePath.getText();
				String outFilePathStr = chooseOutPath.getText();
				if (checkPath(filePathStr, outFilePathStr)) {
					start.dealNew(filePathStr, outFilePathStr);
				}
			}
		});
		newData.setBounds(153, 139, 110, 23);
		panel.add(newData);

		JButton notD10Close = new JButton("超过10天没close");
		notD10Close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filePathStr = filePath.getText();
				String outFilePathStr = chooseOutPath.getText();
				if (checkPath(filePathStr, outFilePathStr)) {
					start.dealD10NotClose(filePathStr, outFilePathStr);
				}
			}
		});
		notD10Close.setBounds(273, 139, 151, 23);
		panel.add(notD10Close);

		JButton all = new JButton("全部处理");
		all.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filePathStr = filePath.getText();
				String outFilePathStr = outFilePath.getText();
				if (checkPath(filePathStr, outFilePathStr)) {
					start.dealAll(filePathStr, outFilePathStr);
				}
			}
		});
		all.setBounds(86, 182, 270, 31);
		panel.add(all);

	}

	private boolean checkPath(String filePath, String outFilePath) {
		boolean flag = false;
		if (filePath.isEmpty()) {
			JOptionPane.showMessageDialog(null, "请选择excel文件！");
			flag = false;
		} else if (outFilePath.isEmpty()) {
			JOptionPane.showMessageDialog(null, "请选择文件存放目录！");
			flag = false;
		} else {
			flag = true;
		}
		return flag;
	}
}
