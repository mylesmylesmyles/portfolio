(ns portfolio.core
  (:require [clojure.browser.event :as event]
            [clojure.string :as string]
            [tailrecursion.javelin :refer [cell cell=]]
            [dommy.core :as dommy])
  (:use [cljs.reader :only [read-string]]
        [domina :only [add-class! by-id remove-class! set-text! set-html! swap-content! single-node]]
        [domina.css :only [sel]]
        [domina.events :only [capture! listen!]]
        [domina.xpath :only [xpath]])
  (:require-macros [tailrecursion.javelin :refer [defc defc=]]
                   [dommy.macros :refer [node]]))

; (def ENTER-KEY 13)

; Purchased stocks
(defc bought [])

; Sold stocks
(defc sold [])

; Dividends received
(defc dividends [])

; symbol : display : shares : current-price : cost
(defc shares {})



; one page for the summary
; a page for active investments
; a page for sold investments
; a page for dividends

; summary
; current gain/loss $ : current gain/loss % : current value $
; past gain/loss $ : past gain/loss % : past value $

; Add shares
(defn add-shares! [stock_symbol shares]
    (when empty? (shares (symbol stock_symbol))
  		(swap! shares conj (symbol stock_symbol) [])))

(defn remove-shares! [stock_symbol shares])

; active investments
; symbol : name : # of shares : bought at : current price : current value : bought on : commission : gain/loss $ : gain/loss %
(defn buy! [stock_symbol display shares purchase_price current_price bought_on commission]
	"Purchase a number of shares in a stock"
	(let [shares_cell (cell shares)
			purchase_price_cell (cell purchase_price)
			current_price_cell (cell current_price)
			commission_cell (cell commission)
			current_value_cell (cell= (* shares_cell current_price_cell))
			gain_loss_dollar_cell (cell= (- current_value_cell (* shares_cell purchase_price_cell)))
			gain_loss_pct_cell nil]
		(swap! bought conj {:symbol stock_symbol
                      		:display display
	                        :shares shares_cell 
							:purchase_price purchase_price_cell 
							:current_price current_price_cell 
							:bought_on bought_on
							:commission commission_cell 
							:current_value current_value_cell 
							:gain_loss_dollar gain_loss_dollar_cell 
							:gain_loss_pctc gain_loss_pct_cell})
  		(add-shares! stock_symbol shares)))

(defn sell! [stock shares price sold_on commission]
  "Sell a number of shares in a particular stock"
  nil)

; sold investments
; symbol : name : # of shares : bought at : sold price : current value : bought on : sold on : commission : gain/loss $ : gain/loss %

; dividends
; symbol : amount : on date