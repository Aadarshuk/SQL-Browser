package SwingComp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SqlBrowser extends JFrame
{
		JLabel jQuery,jResult,jLogin;
		JButton jButtonRun,jButtonClear;
		JTextArea jTextAreaQuery,jTextAreaOutput;
		JScrollPane jScrollPane,jScrollPaneQuery;
		JTextField jUserName,jPassword;
		Font font;
		Image icon;
		String query,username,password;
		Connection connection = null;
		Statement statement = null;
		ResultSet rSet = null;
		PreparedStatement pStatement = null;
		
			public SqlBrowser()
			{
				font = new Font("Century Schoolbook", Font.PLAIN, 16);
				jQuery = new JLabel("Enter Sql Statement");
				jResult = new JLabel("Results");
				jLogin = new JLabel("Database Credentials : ");
				
				jUserName = new JTextField("username");
				jPassword = new JTextField("password");
				
				jTextAreaQuery = new JTextArea();
				jTextAreaQuery.setFont(font);
				jScrollPaneQuery = new JScrollPane(jTextAreaQuery);
				
				jTextAreaOutput = new JTextArea();
				
				jTextAreaOutput.setEditable(false);
				jScrollPane = new JScrollPane(jTextAreaOutput);
				
				jButtonRun = new JButton("Run");
				jButtonClear = new JButton("Clear");
				
				jLogin.setBounds(20, 5, 131, 50);
				add(jLogin);
				
				jUserName.setBounds(170, 17, 170, 29);
				jUserName.setToolTipText("Enter username");
				add(jUserName);
				
				jPassword.setBounds(350, 17, 170, 29);
				jPassword.setToolTipText("Enter password");
				add(jPassword);
				
				jQuery.setBounds(20, 62, 150, 50);
				add(jQuery);
				
				jButtonClear.setBounds(380, 65, 70, 28);
				jButtonClear.setBackground(Color.BLACK);
				jButtonClear.setForeground(Color.WHITE);
				add(jButtonClear);
				
				jButtonRun.setBounds(460, 65, 59, 28);
				jButtonRun.setBackground(Color.RED);
				jButtonRun.setForeground(Color.WHITE);
				add(jButtonRun);
				
				jScrollPaneQuery.setBounds(20, 105, 500, 90);
				add(jScrollPaneQuery);
				
				jResult.setBounds(20,180,50,50);
				add(jResult);
				
				jScrollPane.setBounds(20, 220, 500, 330);
				add(jScrollPane);
				
				icon = Toolkit.getDefaultToolkit().getImage("sql_icon.png");
				
				setSize(555,600);
				setIconImage(icon);
				setTitle("SQL Browser");
				getContentPane().setBackground(Color.WHITE);
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				setLayout(null);
				setVisible(true);
				
				jButtonClear.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						jTextAreaQuery.setText("");
						
					}
				});
			
				jButtonRun.addActionListener(new ActionListener() {		
					@Override
					public void actionPerformed(ActionEvent e) {
					
						jTextAreaOutput.setText("");
						jTextAreaOutput.setFont(font);
						jTextAreaOutput.setForeground(Color.BLACK);
						query = jTextAreaQuery.getText();
						username = jUserName.getText().toString();
						password = jUserName.getText().toString();
						if(username!=null && password!=null)
     					Connect();
						else
							jTextAreaOutput.append("please enter username & password");
					}
				});
			}
			
			protected void Connect() {
				try{
					Class.forName("oracle.jdbc.driver.OracleDriver");
					connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",username,password);
					statement = connection.createStatement();
					boolean queryType = statement.execute(query);
					
					if(queryType){
						rSet = statement.executeQuery(query);
						ResultSetMetaData rData = rSet.getMetaData(); 
						int columns = rData.getColumnCount();
						int counter=0;
						jTextAreaOutput.setFont(font);
						while (rSet.next()) 
						{	
							for(int i=1; i<=columns; i++)
							{
								jTextAreaOutput.append("\n"+rData.getColumnName(i)+" : "+rSet.getString(i));
							}
							jTextAreaOutput.append("\n-------------------------------------");
							counter++;
						}
						jTextAreaOutput.append("\n\n"+Integer.toString(counter)+" Rows selected.");
					}
					else{
						int count = statement.getUpdateCount();
						jTextAreaOutput.setFont(font);
						
						if(count==1)
							jTextAreaOutput.append(Integer.toString(count)+" records affected.");
						else
							jTextAreaOutput.append(Integer.toString(count)+" records affected.");	
					}
				}
				catch (SQLException e) {
					e.printStackTrace();
					jTextAreaOutput.setFont(new Font("Century Schoolbook", Font.ITALIC, 15));
					jTextAreaOutput.setForeground(Color.RED);
					jTextAreaOutput.append(e.getMessage());
				}
				
				catch (ClassNotFoundException c) {
					c.printStackTrace();
				}
				finally
				{
					try {
						if(connection!=null)
							connection.close();
						if(rSet!=null)
							rSet.close();
						if(statement!=null)
							statement.close();
					} catch (SQLException e) {	
						e.printStackTrace();
					}
				}
		}
			
}