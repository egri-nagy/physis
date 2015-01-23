plot.statistics <- function (filename) 
{
    print("Reading statistics table...");
    d <- read.table(filename, header = TRUE);

    j <- 1;
    for (i in names(d)) {
        fout <- paste(i, ".eps", sep = "");
    	print(paste("Drawing graph:",i, " into ", fout, sep = ""));
        postscript(fout, horizontal = FALSE, height = 4, width = 8, pointsize = 12, onefile = FALSE);

        plot(d$update, d[,j], type = "l", xlab = "time in update cycles", ylab = i);
        title(i);
        j <- j + 1;
        dev.off();
    }

    fout <- paste("fitness", ".eps", sep = "");
    postscript(fout, horizontal = FALSE, height = 4, width = 8, pointsize = 12, onefile = FALSE);
    plot(d$update, d$maximum.fitnes, type = "l", xlab = "time in update cycles", ylab ="fitness");
    points(d$update, d$average.fitness, type="l");
    title("Maximum and average fitness");
    dev.off();
}
