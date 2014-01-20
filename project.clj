(defproject portfolio "0.0.1-SNAPSHOT"
  :description "A Clojure/Clojurescript application for managing my financial portfolio"
  :url "http://brukru.com/portfolio"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}

  :min-lein-version "2.3.4"

  ;; We need to add src/cljs too, because cljsbuild does not add its
  ;; source-paths to the project source-paths
  :source-paths ["src/clj" "src/cljs"]

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2138"]
                 [ring "1.2.1"]
                 [compojure "1.1.6"]
                 [tailrecursion/javelin "2.4.0"]]

  :plugins [[lein-cljsbuild "1.0.1"]
            [lein-ring "0.8.7"]
            [lein-ancient "0.5.4"]]

  :hooks [leiningen.cljsbuild]
  
  ; :ring {:handler portfolio.routes/app}
  
  ; :aot [portfolio.server]
  
  ; :main portfolio.server

  :cljsbuild
  {:builds {;; This build is only used for including any cljs source
            ;; in the packaged jar when you issue lein jar command and
            ;; any other command that depends on it
            :portfolio
            {:source-paths ["src/cljs"]
             ;; The :jar true option is not needed to include the CLJS
             ;; sources in the packaged jar. This is because we added
             ;; the CLJS source codebase to the Leiningen
             ;; :source-paths
             ;:jar true
             ;; Compilation Options
             :compiler
             {:output-to "dev-resources/public/js/portfolio.js"
              :optimizations :advanced
              :pretty-print false}}}})
