<#-- @ftlvariable name="entries" type="kotlin.collections.List<io.gianluigip.journal.BlogEntry>" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Kotlin Journal</title>
</head>
<body style="text-align: center; font-family: sans-serif">
<img src="/static/ktor.png">
<h1>Kotlin Ktor Journal </h1>
<p><i>Powered by Ktor, kotlinx.html & Freemarker!</i></p>
<hr>
<#list entries as item>
    <div>
        <h3>${item.headline}</h3>
        <p>${item.body}</p>
    </div>
</#list>
<hr>
<div>
    <h3>Add a new journal entry!</h3>
    <form action="/journal/submit" method="post">
        <input type="text" name="headline">
        <br>
        <textarea name="body"></textarea>
        <br>
        <input type="submit">
    </form>
</div>
</body>
</html>