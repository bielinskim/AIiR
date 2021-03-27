package com.bielinskim;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Search {

    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void search(String option, List<String> pages, int levels, boolean onlyCurrentDomain) {
        search(option, pages, levels, onlyCurrentDomain,"");
    }

    public static void search(String option, List<String> pages, int levels, boolean onlyCurrentDomain, String phrase) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Set<String> pagesVisited = new HashSet<String>();
        List<String> pagesToVisit = pages;

        for (int i = 0; i < levels; i++) {
            List<String> nextPagesToVisit = new LinkedList<String>();
            CountDownLatch latch = new CountDownLatch(pagesToVisit.size());
            for (Iterator<String> iterator = pagesToVisit.iterator(); iterator.hasNext(); ) {
                String currentUrl = iterator.next();

                    if(!pagesVisited.contains(currentUrl)) {
                        pagesVisited.add(currentUrl);
                        executorService.execute(() -> {
                            try(final WebClient webClient = new WebClient()) {
                                webClient.getOptions().setJavaScriptEnabled(false);
                                final HtmlPage page = webClient.getPage(currentUrl);
                                System.out.println(ANSI_BLUE + "Przetwarzana strona: " + ANSI_RESET + currentUrl);

                                switch(option) {
                                    case "phrase":
                                        searchByPhrase(page, phrase);
                                        break;
                                    case "ref":
                                        searchReferences(page);
                                        break;
                                    case "img":
                                        searchImages(page);
                                        break;
                                    default:
                                }

                                appendNextPagesToVisit(nextPagesToVisit, page, onlyCurrentDomain);
                                latch.countDown();
                            }
                             catch (FailingHttpStatusCodeException ex) {
                                ex.printStackTrace();
                            }
                            catch (Exception e){
                                System.err.println(e);
                            }
                        });
                    } else {
                        latch.countDown();
                    }
                iterator.remove();
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pagesToVisit = nextPagesToVisit;
        }
        executorService.shutdown();
    }

    public static void searchByPhrase(HtmlPage page, String phrase) {
        List<?> elements = page.getByXPath("//*[text()[contains(.,'"+phrase+"')]]");
        if (!elements.isEmpty()) {
            System.out.println(ANSI_BLUE +"Tytuł strony zawierajacej podana fraze: " + ANSI_RESET + page.getTitleText());
            for (Object element : elements) {
                if (element instanceof DomNode) {
                    System.out.println(ANSI_BLUE + "Caly tekst: " + ANSI_RESET + ((DomNode) element).getTextContent());
                    System.out.println(ANSI_BLUE + "Wezel zawierajacy podana fraze: " + ANSI_RESET + ((DomNode) element).asXml());
                }
            }
        }
    }

    public static void searchReferences(HtmlPage page) {
        List<?> elements = page.getByXPath("//a");
        if (!elements.isEmpty()) {
            for (Object element : elements) {
                if (element instanceof HtmlAnchor) {
                    String href = ((HtmlAnchor) element).getHrefAttribute();
                    System.out.println(ANSI_BLUE + "Odnosnik: "+ ANSI_RESET + href);
                }
            }
        }
    }

    public static void searchImages(HtmlPage page) {
        List<?> elements = page.getByXPath("//img");
        if (!elements.isEmpty()) {
            for (Object element : elements) {
                if (element instanceof HtmlImage) {
                    String src = ((HtmlImage) element).getSrcAttribute();
                    String alt = ((HtmlImage) element).getAltAttribute();
                    System.out.println(alt +" - "+ src);
                }
            }
        }
    }
    public static void appendNextPagesToVisit(List<String> pagesToVisit, HtmlPage page, boolean onlyCurrentDomain) {
        List<?> elements = page.getByXPath("//a");
        String domain = page.getBaseURL().getHost();
        if (!elements.isEmpty()) {
            for (Object element : elements) {
                if (element instanceof HtmlAnchor) {
                    String href = ((HtmlAnchor) element).getHrefAttribute();
                    if(onlyCurrentDomain && href.contains(domain)) {
                        pagesToVisit.add(href);
                    } else if(!onlyCurrentDomain) {
                        pagesToVisit.add(href);
                    }
                }
            }
        }
    }

}
