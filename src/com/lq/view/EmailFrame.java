package com.lq.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.lq.main.MainEmail;
import com.lq.statistics.DataFilter;
import com.lq.util.EmailUtil;
import com.lq.util.PropertiesUtil;
import com.lq.util.TextUtil;

public class EmailFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainEmail start = new MainEmail();
	private PropertiesUtil properties = PropertiesUtil.getPropertiesUtil("config/statistics/local.properties");
	private PropertiesUtil emailProperties = PropertiesUtil.getPropertiesUtil("config/statistics/email.properties");
	private EmailUtil emailUtil = EmailUtil.getEmail(emailProperties.getProperty("hostName"),
			Integer.parseInt(emailProperties.getProperty("port")), emailProperties.getProperty("account"),
			emailProperties.getProperty("password"));
	private JPanel contentPane;
	private JTextField oldPath;
	private JTextField newPath;
	private JTextField outPath;
	private JTextField emailAdd;
	private JTextField alertText;
	private JTextField status;
	private JLabel urgeName;

	String emailStr = "";
	String alertStr = "";
	String creatorStr = "";
	String tickNoStr = "";

	String emailContent = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmailFrame frame = new EmailFrame();
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
	public EmailFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 598, 424);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("CheckBox.light"));
		contentPane.setToolTipText("");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel oldPathText = new JLabel("填写旧表路径:");
		oldPathText.setFont(new Font("宋体", Font.PLAIN, 14));
		oldPathText.setForeground(Color.DARK_GRAY);
		oldPathText.setBounds(37, 27, 104, 41);
		contentPane.add(oldPathText);

		oldPath = new JTextField();
		oldPath.setText(properties.getProperty("oldPath"));
		oldPath.setBounds(151, 39, 304, 29);
		contentPane.add(oldPath);
		oldPath.setColumns(10);

		JLabel lblb = new JLabel("填写新表路径:");
		lblb.setFont(new Font("宋体", Font.PLAIN, 14));
		lblb.setBounds(37, 78, 111, 29);
		contentPane.add(lblb);

		newPath = new JTextField();
		newPath.setText(properties.getProperty("newPath"));
		newPath.setColumns(10);
		newPath.setBounds(151, 82, 304, 29);
		contentPane.add(newPath);

		JButton oldButton = new JButton("...");
		oldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fcDlg = new JFileChooser();
				fcDlg.setDialogTitle("请选择旧的excel文件...");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("excel文件(*.xlsx;)", "xlsx");
				fcDlg.setFileFilter(filter);
				String o = properties.getProperty("oldPath");
				if (o == null || o.isEmpty()) {
					fcDlg.setSelectedFile(new File("D:/."));
				} else {
					fcDlg.setSelectedFile(new File(o));
				}

				int returnVal = fcDlg.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String path = fcDlg.getSelectedFile().getPath();
					oldPath.setText(path);
					properties.editProperty("oldPath", path);
				}
			}
		});
		oldButton.setBackground(new Color(211, 211, 211));
		oldButton.setFont(new Font("宋体", Font.PLAIN, 15));
		oldButton.setForeground(SystemColor.activeCaption);
		oldButton.setBounds(467, 39, 35, 29);
		contentPane.add(oldButton);

		JButton newButton = new JButton("...");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fcDlg = new JFileChooser();
				fcDlg.setDialogTitle("请选择excel文件...");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("excel文件(*.xlsx;)", "xlsx");
				fcDlg.setFileFilter(filter);
				String o = properties.getProperty("newPath");
				if (o == null || o.isEmpty()) {
					fcDlg.setSelectedFile(new File("D:/."));
				} else {
					fcDlg.setSelectedFile(new File(o));
				}
				int returnVal = fcDlg.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String path = fcDlg.getSelectedFile().getPath();
					newPath.setText(path);
					properties.editProperty("newPath", path);
				}
			}
		});
		newButton.setBackground(new Color(211, 211, 211));
		newButton.setFont(new Font("宋体", Font.PLAIN, 15));
		newButton.setForeground(SystemColor.activeCaption);
		newButton.setBounds(467, 81, 35, 29);
		contentPane.add(newButton);

		JLabel label = new JLabel("填写状态:");
		label.setFont(new Font("宋体", Font.PLAIN, 14));
		label.setBounds(65, 117, 92, 29);
		contentPane.add(label);

		JButton statisticsButton = new JButton("统计");
		statisticsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 读数据
				String oldPathStr = oldPath.getText();
				String newPathStr = newPath.getText();
				String outPathStr = outPath.getText();
				String statusStr = status.getText();
				// 检查数据
				if (checkPath(oldPathStr, newPathStr, outPathStr, statusStr)) {
					// 存配置文件
					properties.editProperty("oldPath", oldPathStr);
					properties.editProperty("newPath", newPathStr);
					properties.editProperty("outPath", outPathStr);
					properties.editProperty("status", statusStr);
					boolean flag = start.statistic(oldPathStr, newPathStr, statusStr, outPathStr);
					if (flag) {
						urgeName.setText("统计成功");
					} else {
						urgeName.setText("统计失败");
					}

				}

			}
		});
		statisticsButton.setFont(new Font("宋体", Font.PLAIN, 14));
		statisticsButton.setForeground(new Color(0, 0, 0));
		statisticsButton.setBackground(new Color(100, 149, 237));
		statisticsButton.setBounds(101, 320, 111, 41);
		contentPane.add(statisticsButton);

		JLabel label_1 = new JLabel("填写统计表路径:");
		label_1.setFont(new Font("宋体", Font.PLAIN, 14));
		label_1.setBounds(23, 156, 118, 29);
		contentPane.add(label_1);

		outPath = new JTextField();
		outPath.setText(properties.getProperty("outPath"));
		outPath.setColumns(10);
		outPath.setBounds(151, 156, 304, 29);
		contentPane.add(outPath);

		JButton urgeButton = new JButton("催邮");
		urgeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (DataFilter.notStatusList.size() == 0) {
					JOptionPane.showMessageDialog(null, "请先统计！");
				} else {
					Object[] item = DataFilter.notStatusList.get(0);
					String tickNo = item[1].toString();
					String creator = item[3].toString();
					if ("TheEnd".equals(creator)) {
						JOptionPane.showMessageDialog(null, "邮催结束！");
					} else {
						// 催邮逻辑
						// 每点击一下邮催按钮出现一个人（该人DTS单未转化为指定状态），
						// A框内输入“该人的邮箱”；
						// B款内输入提示语,例如“您的下列DTS单请尽快关闭”。
						// 下一次点击邮催发送上一次邮件；
						// 邮件内容包含：提示语+DTS单号（之间用逗号间隔）

						if (!"".equals(creatorStr)) {
							emailContent = "请" + creatorStr + " " + alertStr + "\r\n			DTS单号:" + tickNoStr + "\r\n";
							// 发送上一次邮件；
							emailUtil.sendSimpleMail(emailStr, alertStr, emailContent);
							// 写txt
							TextUtil.writeTxtFile(emailContent, new File("email记录.txt"), true);
							// 存配置文件
							properties.editProperty("emailAdd", emailStr);
							properties.editProperty("alertText", alertStr);
						}
						// 1 提示 Creator
						urgeName.setText(creator);
						// bbb的邮箱
						// 输入“请关闭”
						// 点击发送邮件，自动发送邮件，邮件内容自动生成如下：bbb请关闭，DTS单：002
						emailStr = emailAdd.getText();
						alertStr = alertText.getText();
						creatorStr = creator;
						tickNoStr = tickNo;
						DataFilter.notStatusList.remove(0);
					}

				}

			}
		});
		urgeButton.setForeground(new Color(0, 0, 0));
		urgeButton.setFont(new Font("宋体", Font.PLAIN, 14));
		urgeButton.setBackground(new Color(100, 149, 237));
		urgeButton.setBounds(250, 320, 111, 41);
		contentPane.add(urgeButton);

		JButton sendEmail = new JButton("发送邮件");
		sendEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				emailContent = "请" + creatorStr + " " + alertStr + "\r\n			DTS单号:" + tickNoStr + "\r\n";
				emailUtil.sendSimpleMail(emailStr, alertStr, emailContent);

				properties.editProperty("emailAdd", emailStr);
				properties.editProperty("alertText", alertStr);

				// 写txt
				TextUtil.writeTxtFile(emailContent, new File("email记录.txt"), true);

			}
		});
		sendEmail.setForeground(new Color(0, 0, 0));
		sendEmail.setFont(new Font("宋体", Font.PLAIN, 14));
		sendEmail.setBackground(new Color(100, 149, 237));
		sendEmail.setBounds(393, 320, 111, 41);
		contentPane.add(sendEmail);

		JButton outButton = new JButton("...");
		outButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fcDlg = new JFileChooser();
				fcDlg.setDialogTitle("请选择文件存放目录");
				fcDlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				String o = properties.getProperty("outPath");
				if (o == null || o.isEmpty()) {
					fcDlg.setSelectedFile(new File("D:/."));
				} else {
					fcDlg.setSelectedFile(new File(o));
				}
				int returnVal = fcDlg.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String path = fcDlg.getSelectedFile().getPath();
					outPath.setText(path);
					properties.editProperty("outPath", path);
				}
			}
		});
		outButton.setForeground(SystemColor.activeCaption);
		outButton.setFont(new Font("宋体", Font.PLAIN, 15));
		outButton.setBackground(new Color(211, 211, 211));
		outButton.setBounds(467, 156, 35, 29);
		contentPane.add(outButton);

		emailAdd = new JTextField();
		emailAdd.setColumns(10);
		emailAdd.setText(properties.getProperty("emailAdd"));

		emailAdd.setBounds(151, 207, 304, 29);
		contentPane.add(emailAdd);

		JLabel label_2 = new JLabel("邮件地址:");
		label_2.setFont(new Font("宋体", Font.PLAIN, 14));

		label_2.setBounds(65, 207, 73, 29);
		contentPane.add(label_2);

		JLabel label_3 = new JLabel("  提示语:");
		label_3.setFont(new Font("宋体", Font.PLAIN, 14));
		label_3.setBounds(65, 246, 73, 29);
		contentPane.add(label_3);

		alertText = new JTextField();
		alertText.setColumns(10);
		alertText.setText(properties.getProperty("alertText"));
		alertText.setBounds(151, 246, 304, 29);
		contentPane.add(alertText);

		urgeName = new JLabel();
		urgeName.setText("...");
		urgeName.setForeground(Color.red);
		urgeName.setFont(new Font("宋体", Font.PLAIN, 15));
		urgeName.setBounds(157, 285, 298, 25);
		contentPane.add(urgeName);

		status = new JTextField();
		status.setText(properties.getProperty("status"));
		status.setColumns(10);
		status.setBounds(151, 121, 304, 29);
		contentPane.add(status);
	}

	private boolean checkPath(String oldPath, String newPath, String outPath, String status) {
		boolean flag = false;
		if (oldPath.isEmpty() || newPath.isEmpty()) {
			JOptionPane.showMessageDialog(null, "请选择excel文件！");
			flag = false;
		} else if (outPath.isEmpty()) {
			JOptionPane.showMessageDialog(null, "请选择文件存放目录！");
			flag = false;
		} else if (status.isEmpty()) {
			JOptionPane.showMessageDialog(null, "请选择指定状态！");
			flag = false;
		} else {
			flag = true;
		}
		return flag;
	}
}
