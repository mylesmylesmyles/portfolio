(ns portfolio.test
  	(:require [clj-http.client :as client]
              [clojure.edn :as edn]))

(def portfolio (atom {:bought [] :sold [] :dividends [] all-shares {}}))

; Purchased stocks
(def bought (atom []))

; Sold stocks
(def sold (atom []))

; Dividends received
(def dividends (atom []))

; A map of shares in the portfolio
; symbol : {shares : price : cost}
(def all-shares (atom {}))

; A map of share names by symbol
; symbol : name
(def share-names (atom {}))

; http://cliffngan.net/a/13
(def yahoo "http://finance.yahoo.com/d/quotes.csv?s=")

(defn set-share-name! [stock-symbol stock-name]
  	"Set the share name for display"
  	(swap! share-names assoc stock-symbol stock-name))

(defn set-share-price! [stock-symbol price]
  	(swap! all-shares assoc-in [stock-symbol :price] price))

(defn owns-stock? [stock-symbol]
	(contains? @all-shares stock-symbol))

(defn- set-share! [stock-symbol shares price commission]
  	"Add or subtract a number of shares from the portfolio"
  	(if (owns-stock? stock-symbol)
    	(let [new-shares (+ (get-in @all-shares [stock-symbol :shares]) shares)
           	  cost (+ (get-in @all-shares [stock-symbol :cost]) (* shares price) commission)]
        	(swap! all-shares assoc stock-symbol {:shares new-shares :price price :cost cost}))
     	(swap! all-shares assoc stock-symbol {:shares shares :price price :cost (+ (* shares price) commission)})))

(defn buy! [stock-symbol display shares purchase-price bought-on commission]
	"Purchase a number of shares in a stock"
	(swap! bought conj {:symbol stock-symbol
                        :shares shares 
						:purchase-price purchase-price 
						:current-price purchase-price
						:bought-on bought-on
						:commission commission})
 	(set-share! stock-symbol shares purchase-price commission))

(defn sell! [stock-symbol shares sell-price sold-on commission]
	"Sell a number of shares in a stock"
	(swap! sold conj {:symbol stock-symbol
                   	  :shares shares
                      :sell-price sell-price
                      :sold-on sold-on
                      :commission commission})
 	(set-share! stock-symbol (* shares -1) sell-price commission))

(defn add-dividend! [stock-symbol amount on-date]
  	"Add a dividend received for a stock"
	(swap! dividends conj {:symbol stock-symbol
                        	:amount amount
                         	:on-date on-date}))

(defn get-share-cost [stock-symbol]
  	(if (owns-stock? stock-symbol)
     	(get-in @all-shares [stock-symbol :price])
      	0))

(defn get-shares [stock-symbol]
  	(if (owns-stock? stock-symbol)
     	(get-in @all-shares [stock-symbol :shares])
      	0))

(defn get-share-value [stock-symbol]
  	(if (owns-stock? stock-symbol)
	  	(* (get-shares stock-symbol) (get-share-cost stock-symbol)))
   		0)

(defn get-share-profit [stock-symbol]
  	(if (owns-stock? stock-symbol)
	  	(- (get-share-value stock-symbol) (get-share-cost stock-symbol)))
   		0)

(defn get-share-profit-pct [stock-symbol]
  	(if (owns-stock? stock-symbol)
     	(let [svalue (get-share-value stock-symbol)
              scost (get-share-cost stock-symbol)]
        	(->
           		(/ svalue scost)
             	(- 1)
              	(* -100)))
      	0))

(defn get-portfolio-value []
  	"Return the total value of the porfolio"
	(reduce + (map get-share-value (keys @all-shares))))

(defn get-dividend-total
  	([]
    (reduce (fn [total current]
            	(+ total (current :amount)))
            0 @dividends)) 
  	([stock-symbol]
  	(reduce (fn [total current] 
            	(if (= (current :symbol) stock-symbol)
               		(+ total (current :amount))
                 	0)) 
           	0 @dividends)))

(defn get-current-stock-price [stock-symbol]
  	"Return the current stock price or nil if there was an error"
  	(try
		(-?> 
    		(str yahoo stock-symbol "&f=l1") 
	     	client/get 
	      	(:body) 
	       	(#(re-find #"^\d+\.\d+" %1)) 
	        Double/parseDouble)
     	(catch Exception e (nil))))

(defn save-portfolio! [])