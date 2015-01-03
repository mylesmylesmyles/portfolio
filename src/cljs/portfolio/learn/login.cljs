(ns portfolio.learn.login)

;; define the function to be attached to the form submission event
(defn validate-form []
  ;; get the email and password from their ids in the HTML form
  (let [email (.getElementById js/document "email")
        password (.getElementById js/document "password")]
    (if (and (> (count (.-value email)) 0)
             (> (count (.-value password)) 0))
      true
      (do (js/alert "Please complete the form")
        false))))

;; define a function to attach validate-form to the onsubmit event of the form
(defn init[]
  ;; verify that js/document exists and that it has a getElementById property
  (if (and js/document
           (.-getElementById js/document))
    ;; get the loginform element by id and set its onsubmit property to our
    ;; validate-form function
    (if-let [login-form (.getElementById js/document "loginform")]
      (set! (.-onsubmit login-form) validate-form))))

;; initialize the html page in an unobtrusive way
(set! (.-onload js/window) init)