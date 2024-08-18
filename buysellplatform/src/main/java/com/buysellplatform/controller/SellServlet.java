package com.buysellplatform.controller;

import com.buysellplatform.dao.ProductDAO;
import com.buysellplatform.model.Product;
import com.buysellplatform.model.User;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

//import javax.servlet.ServletException;
//import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import jakarta.servlet.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
//@WebServlet("/sell")
@MultipartConfig
public class SellServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;
    private static final String UPLOAD_DIRECTORY = "uploads";

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        try {
            List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest((RequestContext) request);
            String title = null, description = null, minBidPriceStr = null, auctionEndDate = null, fileName = null;

            for (FileItem item : multiparts) {
                if (!item.isFormField()) {
                    fileName = new File(item.getName()).getName();
                    File file = new File(uploadPath + File.separator + fileName);
                    item.write(file);
                } else {
                    switch (item.getFieldName()) {
                        case "title":
                            title = item.getString();
                            break;
                        case "description":
                            description = item.getString();
                            break;
                        case "minBidPrice":
                            minBidPriceStr = item.getString();
                            break;
                        case "auctionEndDate":
                            auctionEndDate = item.getString();
                            break;
                    }
                }
            }

            if (title != null && description != null && minBidPriceStr != null && auctionEndDate != null) {
                double minBidPrice = Double.parseDouble(minBidPriceStr);
                User seller = (User) request.getSession().getAttribute("user");

                Product product = new Product();
                product.setTitle(title);
                product.setDescription(description);
                product.setMinBidPrice(minBidPrice);
                product.setAuctionEndDate(auctionEndDate);
                product.setImage(fileName);
                product.setSellerId(seller.getId());

                boolean isProductListed = productDAO.listProduct(product);

                if (isProductListed) {
                    response.sendRedirect("product-listing.jsp");
                } else {
                    request.setAttribute("errorMessage", "Product listing failed. Please try again.");
                    request.getRequestDispatcher("sell.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "All fields are required.");
                request.getRequestDispatcher("sell.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            request.setAttribute("errorMessage", "File upload failed due to: " + ex.getMessage());
            request.getRequestDispatcher("sell.jsp").forward(request, response);
        }
    }
}
