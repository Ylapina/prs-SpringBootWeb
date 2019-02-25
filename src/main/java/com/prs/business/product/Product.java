package com.prs.business.product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.prs.business.vendor.Vendor;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	// private int id;
	@ManyToOne
	@JoinColumn(name = "VendorId")
	private Vendor vendor;
	private String partNumber;
	private String name;
	private double price;
	private String unit;
	private String photoPass;

	public Product() {
		super();
	}
    //additional constructor for unit test
	public Product(Vendor vendor, String partNumber, String name, double price, String unit, String photoPass) {
		super();
		this.vendor = vendor;
		this.partNumber = partNumber;
		this.name = name;
		this.price = price;
		this.unit = unit;
		this.photoPass = photoPass;
	}

	public Product(int id, Vendor vendor, String vendorPartNumber, String name, double price, String unit,
			String photoPass) {

		this.id = id;
		// this.vendorId = vendorId;
		this.vendor = vendor;
		this.partNumber = vendorPartNumber;
		this.name = name;
		this.price = price;
		this.unit = unit;
		this.photoPass = photoPass;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public String getVendorPartNumber() {
		return partNumber;
	}

	public void setVendorPartNumber(String vendorPartNumber) {
		this.partNumber = vendorPartNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPhotoPass() {
		return photoPass;
	}

	public void setPhotoPass(String photoPass) {
		this.photoPass = photoPass;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", vendorId=" + vendor + ", vendorPartNumber=" + partNumber + ", name=" + name
				+ ", price=" + price + ", unit=" + unit + ", photoPass=" + photoPass + "]";
	}

}
