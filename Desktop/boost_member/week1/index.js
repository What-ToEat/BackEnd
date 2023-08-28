import path from "path";
import express from "express";

const app = express();
const PORT = 3001;
const __dirname = path.resolve();

app.set('view engine' , 'pug')

app.use(express.static(__dirname + '/public'))
app.use(express.static(__dirname + '/views'))

app.get('/' , (req , res) => {
    res.render("template.pug")
})

app.listen(PORT, () => {
    console.log(`server on ${PORT}`);
});