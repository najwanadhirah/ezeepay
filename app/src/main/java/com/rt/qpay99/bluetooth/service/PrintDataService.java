package com.rt.qpay99.bluetooth.service;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.UnicodeFormatter;
import com.zj.btsdk.PrintPic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.UUID;

public class PrintDataService {
	private Context context = null;
	private String deviceAddress = null;
	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();
	private BluetoothDevice device = null;
	private static BluetoothSocket bluetoothSocket = null;
	private static OutputStream outputStream = null;
	private static final UUID uuid = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	// private static final UUID uuid = UUID.fromString(Config.strUUID);
	private static boolean isConnection;
	final String[] items = { "", "", " ", " ",
			" ", " ", " ", " ", " ", " ", " ",
			" ", " " };
	final byte[][] byteCommands = { { 0x1b, 0x40 },
			{ 0x1b, 0x4d, 0x00 },
			{ 0x1b, 0x4d, 0x01 },
			{ 0x1d, 0x21, 0x00 },
			{ 0x1d, 0x21, 0x11 },
			{ 0x1b, 0x45, 0x00 },
			{ 0x1b, 0x45, 0x01 },
			{ 0x1b, 0x7b, 0x00 },
			{ 0x1b, 0x7b, 0x01 },
			{ 0x1d, 0x42, 0x00 },
			{ 0x1d, 0x42, 0x01 },
			{ 0x1b, 0x56, 0x00 },
			{ 0x1b, 0x56, 0x01 },
			{ 0x1d, 0x6b, 0x49 },
			{ 0x1c, 0x70, 0x00, 0x00 },
			{ 0x0A },// LF 15
			{ 0x1b, 0x44, 0x02, 0x00 },// TAB 16
			{ 0x1d, 0x6b, 0x04, 0x01, 0x30 },// Barcode 39 17
			{ 0x1d, 0x6b, 0x48, 0x01 },// LF 18
			{ 0x1b, 0x61, 0x32 },// Right justification 19
			{ 0x1b, 0x61, 0x00 },// Left justification 20
			{ 0x1b, 0x61, 0x01 },// Centering 21
			{ 0x1d, 0x21, 0x16 },// font height 21 22
			{ 0x1d, 0x48, 0x02 },// HRI Below the bar code 23
			{ 0x1d, 0x66, 0x32 },// HRI 24
			{ 0x1d, 0x68, 0x30 },// Barcode Height 25
			{ 0x1d, 0x77, 0x02 },// Barcode Width 26
			{ 0x1d, 0x48, 0x00 },// HRI Not printed 27
			{ 0x1d, 0x4c, 0x04, 0x02 },// Set left margin 2 28
			{ 0x1d, 0x66, 0x04, 0x03, 0x33 },// Barcode 3 29
			{ 0x1d, 0x66, 0x04, 0x04, 0x34 },// Barcode 4 30
			{ 0x1d, 0x66, 0x04, 0x05, 0x35 },// Barcode 5 31
			{ 0x1d, 0x6b, 0x48, 0x0c, 0x39 },// Barcode ean13 32
			{ 0x1b, 0x21, 0x1e }, //change font 33
			{ 0x1b, 0x33, 0x1e }, //change font 34
			{ 0x1b, 0x21, 0x03 }, //bold 35
			{ 0x1b, 0x21, 0x04 }, //double height  36
			{ 0x1b, 0x21, 0x0e }, //bold  37
			{ 0x1b, 0x21, 0x10 }, //double height  38


			//inner printer command
			{ 0x1d, 0x6b, 0x43,0x39},// Barcode ean13 39
			{ 0x1d, 0x21, 0x00},// Select character size 40
			{ 0x1d, 0x21, 0x11},// Select character size 41
			{ 0x1d, 0x64, 0x02},// Print and feed n lines 42
			{ 0x1b, 0x61, 0x31},// align center 43


			{0x1d},
			{0x1d},
			{0x1d},
			{0x1d},

	};

	private static ProgressDialog pd;

	// 0x1B,0x21,0x31 ESC

	// private static final byte[] CHAR_SIZE_0 = { 0x1D, 0x21, 0x00 };
	// private static final byte[] CHAR_SIZE_1 = { 0x1D, 0x21, 0x01 };
	// private static final byte[] CHAR_SIZE_2 = { 0x1D, 0x21, 0x30 };
	// private static final byte[] CHAR_SIZE_3 = { 0x1D, 0x21, 0x31 };

	public PrintDataService(Context context, String deviceAddress) {
		super();
		this.context = context;
		this.deviceAddress = deviceAddress;
		this.device = this.bluetoothAdapter.getRemoteDevice(this.deviceAddress);

	}


	public String getDeviceName() {
		return this.device.getName();
	}


	private void resetConnection() {
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (Exception e) {
			}
			outputStream = null;
		}

		if (bluetoothSocket != null) {
			try {
				bluetoothSocket.close();
			} catch (Exception e) {
			}
			bluetoothSocket = null;
		}
	}

	public boolean connect() {
		DLog.e("PrintDataService", "connect");

		resetConnection();
		// if (!isConnection) {
		try {
			bluetoothSocket = this.device
					.createRfcommSocketToServiceRecord(uuid);
			bluetoothSocket.connect();
			outputStream = bluetoothSocket.getOutputStream();
			isConnection = true;
			if (this.bluetoothAdapter.isDiscovering()) {
				this.bluetoothAdapter.isDiscovering();
			}
		} catch (Exception e) {
			disconnect();
			DLog.e("TAG", "" + e.getMessage());
			try {
				bluetoothAdapter.cancelDiscovery();
				bluetoothSocket = device
						.createInsecureRfcommSocketToServiceRecord(uuid);
				try {
					Method m = device.getClass().getMethod(
							"createInsecureRfcommSocket",
							new Class[] { int.class });
					bluetoothSocket = (BluetoothSocket) m.invoke(device, 1);
				} catch (NoSuchMethodException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				bluetoothSocket.connect();
				outputStream = bluetoothSocket.getOutputStream();
				isConnection = true;
				if (this.bluetoothAdapter.isDiscovering()) {
					this.bluetoothAdapter.cancelDiscovery();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			//isConnection = false;
			// Toast.makeText(this.context, "Connecting failed  ", 1).show();
			return false;
		}
		// Toast.makeText(this.context,
		// this.device.getName() + "Printer Connected  ",
		// Toast.LENGTH_SHORT).show();

		return true;
		// } else {
		// isConnection = true;
		// return true;
		// }
	}


	public static void disconnect() {
		DLog.e("PrintDataService", "disconnect");
		try {
			if (bluetoothSocket != null) {
				bluetoothSocket.close();
				// bluetoothSocket = null;
			}
			if (outputStream != null) {
				outputStream.close();
				// outputStream = null;
			}
			isConnection = false;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void selectCommand() {
		new AlertDialog.Builder(context).setTitle("Select Command")
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							outputStream.write(byteCommands[which]);
						} catch (IOException e) {
							Toast.makeText(context, "Command setting failed",
									Toast.LENGTH_SHORT).show();
						}
					}
				}).create().show();
	}

	public void setByteCommand(byte[] b) {
		try {
			outputStream.write(b[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void setCommand(int which) {
		try {
			outputStream.write(byteCommands[which]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printImage() {
		byte[] sendData = null;
		PrintPic pg = new PrintPic();
		pg.initCanvas(100);
		pg.initPaint();
		// /storage/emulated/0
		pg.drawImage(0, 0, "/storage/emulated/0/ic_receipt_icon.PNG");
		// pg.drawImage(0, 0, "/mnt/sdcard/tmc.bmp");
		sendData = pg.printDraw();
		// usbCtrl.sendByte(sendData, dev); //
		try {
			outputStream.write(sendData, 0, sendData.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public void printImage(String path) {
//		byte[] sendData = null;
//		PrintPic pg = new PrintPic();
//		pg.initCanvas(100);
//		pg.initPaint();
//		// /storage/emulated/0
//		pg.drawImage(0, 0, path);
//		// pg.drawImage(0, 0, "/mnt/sdcard/tmc.bmp");
//		sendData = pg.printDraw();
//		// usbCtrl.sendByte(sendData, dev); //
//		try {
//			outputStream.write(sendData, 0, sendData.length);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public void printImage2() {
		byte[] sendData = null;
		PrintPic pg = new PrintPic();
		pg.initCanvas(100);
		pg.initPaint();
		// /storage/emulated/0


		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();


		File file = new File(extStorageDirectory, "ic_receipt_icon.PNG");

		pg.drawImage(0, 0,  Environment.getExternalStorageDirectory().getAbsolutePath()  + "/ic_receipt_icon.PNG");
		// pg.drawImage(0, 0, "/mnt/sdcard/tmc.bmp");
		sendData = pg.printDraw();
		// usbCtrl.sendByte(sendData, dev); //
		try {
			outputStream.write(sendData, 0, sendData.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	public void send(String sendData) {
		if (isConnection) {
			if (FunctionUtil.isSet(sendData)) {
				try {
					// outputStream.write(byteCommands[which]);
					// byte[] data = sendData.getBytes("unicode");
					byte[] data = sendData.getBytes();

					outputStream.write(data, 0, data.length);
					// outputStream.flush();
				} catch (IOException e) {
					Toast.makeText(this.context, "Connect Failed  ",
							Toast.LENGTH_SHORT).show();
					isConnection=false;
				}
			}
			// System.out.println("  ʼ  ӡ    " + sendData);

		} else {
//			Toast.makeText(this.context,
//					"Connection failed, Please try again.", Toast.LENGTH_SHORT)
//					.show();

		}
	}

	public void sendBarcode() {
		if (isConnection) {
			try {

				// Setting height
				int gs = 29;
				outputStream.write(intToByteArray(gs));
				int h = 104;
				outputStream.write(intToByteArray(h));
				int n = 162;
				outputStream.write(intToByteArray(n));

				// Setting Width
				int gs_width = 29;
				outputStream.write(intToByteArray(gs_width));
				int w = 119;
				outputStream.write(intToByteArray(w));
				int n_width = 3;
				outputStream.write(intToByteArray(n_width));

				// Print BarCode
				int gs1 = 29;
				outputStream.write(intToByteArray(gs1));
				int k = 107;
				outputStream.write(intToByteArray(k));
				int m = 4;
				outputStream.write(intToByteArray(m));

				String barCodeVal = "!123456789012!";// "HELLO12345678912345012";
				System.out.println("Barcode Length : " + barCodeVal.length());
				int n1 = barCodeVal.length();
				outputStream.write(intToByteArray(n1));

				// for (int i = 0; i < barCodeVal.length(); i++) {
				// outputStream.write((barCodeVal.charAt(i) + "").getBytes());
				// }

				byte[] data = barCodeVal.getBytes();
				DLog.e("sendBarcode ", "data " + data);
				outputStream.write(data, 0, data.length);

				outputStream.flush();
			} catch (IOException e) {
				Toast.makeText(this.context, "Connect Failed  ",
						Toast.LENGTH_SHORT).show();
				//isConnection=false;
				connect();
			}
		} else {
			Toast.makeText(this.context,
					"Connection failed, Please try again.", Toast.LENGTH_SHORT)
					.show();

		}
	}

	public static byte intToByteArray(int value) {
		byte[] b = ByteBuffer.allocate(4).putInt(value).array();
		DLog.e("intToByteArray ", "value" + value);
		for (int k = 0; k < b.length; k++) {
			DLog.e("intToByteArray ", "0x" + UnicodeFormatter.byteToHex(b[k]));
			//
			// System.out.println("Selva  [" + k + "] = " + "0x"
			// + UnicodeFormatter.byteToHex(b[k]));
		}

		return b[3];
	}

	public byte[] sel(int val) {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putInt(val);
		buffer.flip();
		return buffer.array();
	}

	public void printLogo(String strPath) {
		ByteArrayOutputStream blob = new ByteArrayOutputStream();
		if (isConnection) {
			// System.out.println("printLogo  ʼ  ӡ    " + strPath);
			// Bitmap imageBit = BitmapFactory.decodeResource(
			// context.getResources(), R.drawable.ic_launcher);
			// imageBit.compress(CompressFormat.PNG, 0, blob);
			// byte[] bitmapdata = blob.toByteArray();

			File f = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/7Eleven.bmp");
			DLog.e("TAG", "path = " + f.getAbsolutePath());
			Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
			bmp.compress(Bitmap.CompressFormat.JPEG, 80, blob);
			byte[] bitmapdata2 = blob.toByteArray();

			try {
				outputStream.write(bitmapdata2, 0, bitmapdata2.length);
				outputStream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(this.context, "printLogo failed",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void sendBarcode(String barCodeVal) {
		if (this.isConnection) {
			System.out.println("sendBarcode");
			try {
				// Print BarCode
				int gs1 = 29;
				outputStream.write(intToByteArray(gs1));
				int k = 107;
				outputStream.write(intToByteArray(k));
				int m = 73;
				outputStream.write(intToByteArray(m));
				int n1 = barCodeVal.length();
				outputStream.write(intToByteArray(n1));

				for (int i = 0; i < barCodeVal.length(); i++) {
					outputStream.write((barCodeVal.charAt(i) + "").getBytes());
					outputStream.flush();
				}
			} catch (IOException e) {
				Toast.makeText(this.context, "Print failed.",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this.context,
					"Connection failed, Please try again.", Toast.LENGTH_SHORT)
					.show();

		}
	}

	public static boolean isPrinterConnected() {
		return isConnection;
	}
	public static void setPrinterDisconnected() {
		isConnection=false;
	}
}
