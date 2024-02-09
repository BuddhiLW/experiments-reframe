package main

import (
	"database/sql"
	"io/ioutil"
	"net/http"
	"strings"

	_ "github.com/go-sql-driver/mysql"
	"github.com/google/uuid"
	"github.com/rs/cors"
)

func main() {

	mux := http.NewServeMux()

	cors := cors.New(cors.Options{
		AllowedOrigins: []string{"*"},
		AllowedMethods: []string{
			http.MethodPost,
			http.MethodGet,
		},
		AllowedHeaders:   []string{"*"},
		AllowCredentials: false,
	})

	mux.HandleFunc("/upload", uploadFile)
	mux.HandleFunc("/files", getFiles)
	mux.HandleFunc("/file", getFile)

	handler := cors.Handler(mux)

	http.ListenAndServe(":8080", handler)
}

type File struct {
	ID   string
	Name string
	Data []byte
}

func ConnectDB() *sql.DB {
	db, err := sql.Open("mysql", "user:password@tcp(127.0.0.1:3301)/dbname")
	if err != nil {
		// handle this error
	}
	defer db.Close()
	return db
}

func uploadFile(w http.ResponseWriter, r *http.Request) {
	db := ConnectDB()
	defer db.Close()
	r.ParseMultipartForm(10 << 20)

	file, handler, err := r.FormFile("file")
	if err != nil {
		// handle this error
	}
	defer file.Close()

	bytes, err := ioutil.ReadAll(file)
	if err != nil {
		// handle this error
	}

	_, err = db.Exec("INSERT INTO files (id, name, data) VALUES (?, ?)", uuid.New().String(), handler.Filename, bytes)
	if err != nil {
		// handle this error
	}

	w.Write([]byte("File uploaded successfully!"))
}

func getFiles(w http.ResponseWriter, r *http.Request) {
	db := ConnectDB()
	defer db.Close()
	rows, err := db.Query("SELECT name FROM files")
	if err != nil {
		// handle this error
	}
	defer rows.Close()

	var files []string
	for rows.Next() {
		var name string
		err = rows.Scan(&name)
		if err != nil {
			// handle this error
		}
		files = append(files, name)
	}

	w.Write([]byte("Files: " + strings.Join(files, ", ")))
}

func getFile(w http.ResponseWriter, r *http.Request) {
	name := r.URL.Query().Get("name")
	db := ConnectDB()
	defer db.Close()

	var data []byte
	err := db.QueryRow("SELECT data FROM files WHERE name = ?", name).Scan(&data)
	if err != nil {
		// handle this error
	}

	w.Write(data)
}
