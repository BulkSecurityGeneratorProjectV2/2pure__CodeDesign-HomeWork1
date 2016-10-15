package ru.sbt.bit.ood.hw1;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

public class TradesJob {

    private final TradesDAO tradesDAO;

    public TradesJob(TradesDAO tradesDAO) {
        this.tradesDAO = tradesDAO;
    }

    public void run() {
        TradesDownloader tradesDownloader = new TradesDownloader();
        String filename = tradesDownloader.downloadFromFTP("localhost", 8090, "foo", "password");
        TradesParser tradesParser = new TradesParser();
        Iterable<CSVRecord> tradeRecords = tradesParser.parseCSV(filename);
        updateTrades(tradeRecords);
    }


    private void updateTrades(Iterable<CSVRecord> trades) {
        tradesDAO.deleteAll();
        for (CSVRecord tradeRecord : trades) {
            Trade trade = new Trade(tradeRecord.toMap());
            tradesDAO.save(trade);
        }
    }


}
